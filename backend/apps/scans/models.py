import uuid

from django.conf import settings
from django.core.validators import MaxValueValidator, MinValueValidator
from django.db import models
from django.utils import timezone


class Scan(models.Model):
    """One plant scan and the disease prediction the AI returned for it."""

    id = models.UUIDField(primary_key=True, default=uuid.uuid4, editable=False)
    user = models.ForeignKey(settings.AUTH_USER_MODEL, on_delete=models.CASCADE, related_name="scans")
    image = models.ImageField(upload_to="scans/%Y/%m/", blank=True, null=True)
    plant_name = models.CharField(max_length=100, blank=True)
    disease = models.CharField(max_length=150, blank=True, help_text="Top predicted disease label.")
    confidence = models.FloatField(
        null=True,
        blank=True,
        validators=[MinValueValidator(0.0), MaxValueValidator(1.0)],
        help_text="Confidence of the top prediction, from 0 to 1.",
    )
    is_healthy = models.BooleanField(default=False)
    predictions = models.JSONField(
        default=list,
        blank=True,
        help_text='All predictions, e.g. [{"label": "Leaf rust", "confidence": 0.91}].',
    )
    recommendation = models.TextField(blank=True, help_text="Treatment or care advice.")
    scanned_at = models.DateTimeField(default=timezone.now, help_text="When the scan was taken on the device.")
    created_at = models.DateTimeField(auto_now_add=True)

    class Meta:
        ordering = ["-scanned_at"]
        indexes = [models.Index(fields=["user", "-scanned_at"])]

    def __str__(self):
        return f"{self.plant_name or 'Plant'} - {self.disease or 'unknown'} ({self.scanned_at:%Y-%m-%d})"
