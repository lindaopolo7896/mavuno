import json

from rest_framework import serializers

from apps.scans.models import Scan


class PredictionsField(serializers.JSONField):
    """Accepts a JSON list, or a JSON string of one (needed for multipart uploads with an image)."""

    def to_internal_value(self, data):
        if isinstance(data, str):
            try:
                data = json.loads(data)
            except ValueError:
                raise serializers.ValidationError("Must be valid JSON.")
        data = super().to_internal_value(data)
        if not isinstance(data, list):
            raise serializers.ValidationError("Must be a list of predictions.")
        for item in data:
            if not isinstance(item, dict) or "label" not in item:
                raise serializers.ValidationError('Each prediction must be an object with a "label".')
        return data


class ScanSerializer(serializers.ModelSerializer):
    predictions = PredictionsField(required=False)

    class Meta:
        model = Scan
        fields = (
            "id",
            "image",
            "plant_name",
            "disease",
            "confidence",
            "is_healthy",
            "predictions",
            "scanned_at",
            "created_at",
        )
        read_only_fields = ("id", "created_at")


class PredictScanSerializer(serializers.Serializer):
    """Upload for POST /scans/predict/."""

    image = serializers.ImageField()
    scanned_at = serializers.DateTimeField(required=False)

    def validate_image(self, image):
        from django.conf import settings

        limit = settings.AI_MAX_UPLOAD_MB * 1024 * 1024
        if image.size > limit:
            raise serializers.ValidationError(f"Image must be {settings.AI_MAX_UPLOAD_MB} MB or smaller.")
        return image
