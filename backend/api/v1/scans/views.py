from rest_framework import mixins, viewsets
from rest_framework.parsers import FormParser, JSONParser, MultiPartParser

from services import scans as scan_service

from .serializers import ScanSerializer


class ScanViewSet(
    mixins.CreateModelMixin,
    mixins.ListModelMixin,
    mixins.RetrieveModelMixin,
    mixins.DestroyModelMixin,
    viewsets.GenericViewSet,
):
    """Save scan results and view your scan history (newest first)."""

    serializer_class = ScanSerializer
    parser_classes = [JSONParser, MultiPartParser, FormParser]

    def get_queryset(self):
        return scan_service.get_user_scans(self.request.user)

    def perform_create(self, serializer):
        serializer.instance = scan_service.create_scan(user=self.request.user, **serializer.validated_data)

    def perform_destroy(self, instance):
        scan_service.delete_scan(instance)
