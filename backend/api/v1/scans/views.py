from django_filters.rest_framework import DjangoFilterBackend
from rest_framework import filters, mixins, status, viewsets
from rest_framework.decorators import action
from rest_framework.parsers import FormParser, JSONParser, MultiPartParser
from rest_framework.response import Response

from services import scans as scan_service
from services.ai import InvalidImage, ModelNotAvailable

from .filters import ScanFilter
from .serializers import PredictScanSerializer, ScanSerializer


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
    filter_backends = [DjangoFilterBackend, filters.SearchFilter]
    filterset_class = ScanFilter
    search_fields = ["plant_name", "disease"]

    def get_queryset(self):
        return scan_service.get_user_scans(self.request.user)

    def get_serializer_class(self):
        if self.action == "predict":
            return PredictScanSerializer
        return ScanSerializer

    def perform_create(self, serializer):
        serializer.instance = scan_service.create_scan(user=self.request.user, **serializer.validated_data)

    def perform_destroy(self, instance):
        scan_service.delete_scan(instance)

    @action(detail=False, methods=["post"], parser_classes=[MultiPartParser, FormParser])
    def predict(self, request):
        """Upload a leaf photo. The AI diagnoses it and the result is saved to your history."""
        serializer = PredictScanSerializer(data=request.data)
        serializer.is_valid(raise_exception=True)
        try:
            prediction, scan = scan_service.predict_and_save_scan(user=request.user, **serializer.validated_data)
        except InvalidImage as error:
            return Response({"image": [str(error)]}, status=status.HTTP_400_BAD_REQUEST)
        except ModelNotAvailable:
            return Response(
                {"detail": "Scanning is temporarily unavailable."}, status=status.HTTP_503_SERVICE_UNAVAILABLE
            )

        if scan is None:
            return Response(
                {
                    "code": "low_confidence",
                    "detail": "We couldn't identify this plant clearly. Take a close, well-lit photo of a single leaf.",
                    "predictions": prediction.top,
                },
                status=status.HTTP_422_UNPROCESSABLE_ENTITY,
            )
        return Response(ScanSerializer(scan, context={"request": request}).data, status=status.HTTP_201_CREATED)
