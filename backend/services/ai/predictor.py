"""Loads the trained model once and runs it on uploaded photos.

Inference steps, matching how the model was trained in ResNet18_Base.ipynb:
1. rebuild an empty ResNet18 with 38 outputs
2. load the trained weights
3. prepare the photo exactly like eval_transform in training
4. run the model and turn scores into probabilities (softmax)
5. map each position to its class name
"""
import threading
from dataclasses import dataclass, field
from pathlib import Path

import torch
from django.conf import settings
from PIL import Image, ImageOps, UnidentifiedImageError
from torch import nn
from torchvision import models, transforms

from .classes import CLASSES

# Must match eval_transform used in training (ResNet18_Base.ipynb).
IMAGENET_MEAN = (0.485, 0.456, 0.406)
IMAGENET_STD = (0.229, 0.224, 0.225)
PREPROCESS = transforms.Compose([
    transforms.Resize((224, 224)),
    transforms.ToTensor(),
    transforms.Normalize(IMAGENET_MEAN, IMAGENET_STD),
])


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
    confidence: float     # 0 to 1
    is_confident: bool    # confidence >= AI_CONFIDENCE_THRESHOLD
    top: list = field(default_factory=list)   # top-k predictions, best first


_model = None
_lock = threading.Lock()


def _load_model():
    global _model
    if _model is not None:
        return _model
    with _lock:
        if _model is None:
            path = Path(settings.AI_MODEL_PATH)
            if not path.is_file():
                raise ModelNotAvailable(f"Model file not found at {path}. Set AI_MODEL_PATH in .env.")
            model = models.resnet18(weights=None)                 # step 1: empty ResNet18
            model.fc = nn.Linear(model.fc.in_features, len(CLASSES))
            state = torch.load(path, map_location="cpu", weights_only=True)
            model.load_state_dict(state)                          # step 2: trained weights
            model.eval()
            _model = model
    return _model


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
    model = _load_model()
    try:
        image = Image.open(file)
        image = ImageOps.exif_transpose(image)   # phone photos store rotation in EXIF
        image = image.convert("RGB")
    except (UnidentifiedImageError, OSError) as error:
        raise InvalidImage("Upload a valid image file.") from error

    batch = PREPROCESS(image).unsqueeze(0)                      # step 3: shape (1, 3, 224, 224)
    with torch.inference_mode():
        probabilities = torch.softmax(model(batch), dim=1)[0]   # step 4
    values, indices = probabilities.topk(settings.AI_TOP_K)

    top = [_as_dict(i, p) for p, i in zip(values.tolist(), indices.tolist())]   # step 5
    best = top[0]
    return Prediction(
        label=best["label"],
        plant_name=best["plant_name"],
        disease=best["disease"],
        is_healthy=best["is_healthy"],
        confidence=best["confidence"],
        is_confident=best["confidence"] >= settings.AI_CONFIDENCE_THRESHOLD,
        top=top,
    )
