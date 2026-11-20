"""Loads the trained model once and runs it on uploaded photos.

Inference steps, matching how the model was trained in ResNet18_v2.ipynb:
1. rebuild an empty ResNet18 with one output per class (39, or 38 for the older model)
2. load the trained weights
3. prepare the photo exactly like eval_transform in training
4. run the model and turn scores into probabilities (softmax)
5. map each position to its class name
6. decide whether to accept the result (see Prediction.rejection)

The similarity check (AI_CHECK_PATH, from section 8 of the notebook) compares the photo's
512 features with the average features of the predicted class. Photos too far from every
leaf the model was trained on are rejected even when the model is confident.
"""
import logging
import threading
from dataclasses import dataclass, field
from pathlib import Path

import torch
from django.conf import settings
from PIL import Image, ImageOps, UnidentifiedImageError
from torch import nn
from torch.nn import functional as F
from torchvision import models, transforms

from .classes import CLASSES, LEAF_CLASS_COUNT, NOT_A_LEAF

logger = logging.getLogger(__name__)

# Must match eval_transform used in training.
IMAGENET_MEAN = (0.485, 0.456, 0.406)
IMAGENET_STD = (0.229, 0.224, 0.225)
PREPROCESS = transforms.Compose([
    transforms.Resize((224, 224)),
    transforms.ToTensor(),
    transforms.Normalize(IMAGENET_MEAN, IMAGENET_STD),
])

# Why a photo was not accepted. None means accepted.
NOT_A_LEAF_REJECTION = "not_a_leaf"         # the model's top answer is Not_a_leaf
UNFAMILIAR_REJECTION = "unfamiliar"         # failed the similarity check
LOW_CONFIDENCE_REJECTION = "low_confidence"  # below AI_CONFIDENCE_THRESHOLD


class ModelNotAvailable(Exception):
    """The model file is missing or could not be loaded."""


class InvalidImage(ValueError):
    """The upload is not an image Pillow can read."""


@dataclass
class Prediction:
    label: str
    plant_name: str
    disease: str          # empty string when healthy
    is_healthy: bool
    confidence: float     # 0 to 1, of the best leaf class
    top: list = field(default_factory=list)   # top-k leaf classes, best first (never Not_a_leaf)
    not_a_leaf_confidence: float | None = None   # None for the 38-class model
    similarity: float | None = None              # None when the similarity check is off
    rejection: str | None = None                 # None when accepted, else one of the *_REJECTION values

    @property
    def is_accepted(self) -> bool:
        return self.rejection is None


@dataclass
class _Loaded:
    model: nn.Module
    class_count: int
    centroids: torch.Tensor | None   # (38, 512), unit length
    similarity_threshold: float | None


_loaded = None
_lock = threading.Lock()


def _load_check(class_count):
    """Load the similarity check file, or return (None, None) if it is missing or unusable."""
    if not settings.AI_CHECK_PATH:
        logger.info("Similarity check turned off (AI_CHECK_PATH is empty).")
        return None, None
    path = Path(settings.AI_CHECK_PATH)
    if not path.is_file():
        logger.warning("Similarity check file not found at %s; check disabled.", path)
        return None, None
    if class_count != len(CLASSES):
        logger.warning("Similarity check needs the %d-class model; check disabled.", len(CLASSES))
        return None, None
    check = torch.load(path, map_location="cpu", weights_only=True)
    expected = [label for label, _, _ in CLASSES]
    if list(check["class_names"]) != expected:
        raise ModelNotAvailable(f"{path} was made for different classes than services/ai/classes.py.")
    centroids = F.normalize(check["centroids"].float(), dim=1)
    if centroids.shape[0] != LEAF_CLASS_COUNT:
        raise ModelNotAvailable(f"{path} has {centroids.shape[0]} class averages, expected {LEAF_CLASS_COUNT}.")
    return centroids, float(check["threshold"])


def _load():
    global _loaded
    if _loaded is not None:
        return _loaded
    with _lock:
        if _loaded is None:
            path = Path(settings.AI_MODEL_PATH)
            if not path.is_file():
                raise ModelNotAvailable(f"Model file not found at {path}. Set AI_MODEL_PATH in .env.")
            if settings.AI_TORCH_THREADS:
                torch.set_num_threads(settings.AI_TORCH_THREADS)
            state = torch.load(path, map_location="cpu", weights_only=True)
            class_count = state["fc.weight"].shape[0]
            if class_count not in (LEAF_CLASS_COUNT, len(CLASSES)):
                raise ModelNotAvailable(
                    f"{path} has {class_count} outputs; expected {LEAF_CLASS_COUNT} or {len(CLASSES)}.")
            model = models.resnet18(weights=None)                     # step 1: empty ResNet18
            model.fc = nn.Linear(model.fc.in_features, class_count)
            model.load_state_dict(state)                              # step 2: trained weights
            model.eval()
            centroids, threshold = _load_check(class_count)
            _loaded = _Loaded(model, class_count, centroids, threshold)
    return _loaded


def _features(model, batch):
    """The 512 numbers ResNet18 produces just before its last layer."""
    x = model.maxpool(model.relu(model.bn1(model.conv1(batch))))
    x = model.layer4(model.layer3(model.layer2(model.layer1(x))))
    return torch.flatten(model.avgpool(x), 1)


def _as_dict(index, probability):
    label, plant, disease = CLASSES[index]
    return {
        "label": label,
        "plant_name": plant,
        "disease": disease or "",
        "is_healthy": disease is None,
        "confidence": round(float(probability), 4),
    }


def predict_image(file) -> Prediction:
    """Run the model on an image file (path or file-like object)."""
    loaded = _load()
    try:
        image = Image.open(file)
        image = ImageOps.exif_transpose(image)   # phone photos store rotation in EXIF
        image = image.convert("RGB")
    except (UnidentifiedImageError, OSError) as error:
        raise InvalidImage("Upload a valid image file.") from error

    batch = PREPROCESS(image).unsqueeze(0)                          # step 3: shape (1, 3, 224, 224)
    with torch.inference_mode():
        features = _features(loaded.model, batch)
        probabilities = torch.softmax(loaded.model.fc(features), dim=1)[0]   # step 4

    leaf_probabilities = probabilities[:LEAF_CLASS_COUNT]
    values, indices = leaf_probabilities.topk(min(settings.AI_TOP_K, LEAF_CLASS_COUNT))
    top = [_as_dict(i, p) for p, i in zip(values.tolist(), indices.tolist())]   # step 5
    best = top[0]

    not_a_leaf_confidence = None
    if loaded.class_count > LEAF_CLASS_COUNT:
        not_a_leaf_confidence = round(float(probabilities[LEAF_CLASS_COUNT]), 4)

    similarity = None
    if loaded.centroids is not None:
        summary = F.normalize(features, dim=1)[0]
        similarity = round(float(summary @ loaded.centroids[indices[0]]), 4)

    # step 6: the first failed check decides the reason
    if CLASSES[int(probabilities.argmax())][0] == NOT_A_LEAF:
        rejection = NOT_A_LEAF_REJECTION
    elif similarity is not None and similarity < loaded.similarity_threshold:
        rejection = UNFAMILIAR_REJECTION
    elif best["confidence"] < settings.AI_CONFIDENCE_THRESHOLD:
        rejection = LOW_CONFIDENCE_REJECTION
    else:
        rejection = None

    return Prediction(
        label=best["label"],
        plant_name=best["plant_name"],
        disease=best["disease"],
        is_healthy=best["is_healthy"],
        confidence=best["confidence"],
        top=top,
        not_a_leaf_confidence=not_a_leaf_confidence,
        similarity=similarity,
        rejection=rejection,
    )
