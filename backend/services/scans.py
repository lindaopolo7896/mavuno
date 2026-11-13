"""Business logic for plant scans."""
from apps.scans.models import Scan


def create_scan(*, user, **data):
    return Scan.objects.create(user=user, **data)


def get_user_scans(user):
    return Scan.objects.filter(user=user)


def delete_scan_images(scans):
    for scan in scans:
        if scan.image:
            scan.image.delete(save=False)


def delete_scan(scan):
    delete_scan_images([scan])
    scan.delete()


def predict_and_save_scan(*, user, image, scanned_at=None):
    """Run the AI on an uploaded photo. Saves a Scan only if the model is confident.

    Returns (prediction, scan). scan is None when the prediction is not confident.
    """
    from services.ai import predict_image

    prediction = predict_image(image)
    if not prediction.is_confident:
        return prediction, None

    image.seek(0)   # the model read the file; rewind so the full image is saved
    extra = {"scanned_at": scanned_at} if scanned_at else {}
    scan = create_scan(
        user=user,
        image=image,
        plant_name=prediction.plant_name,
        disease=prediction.disease,
        confidence=prediction.confidence,
        is_healthy=prediction.is_healthy,
        predictions=prediction.top,
        **extra,
    )
    return prediction, scan
