import django_filters

from apps.scans.models import Scan


class ScanFilter(django_filters.FilterSet):
    plant = django_filters.CharFilter(field_name="plant_name", lookup_expr="iexact")
    is_healthy = django_filters.ChoiceFilter(
        choices=[("true", "true"), ("false", "false")], method="filter_is_healthy"
    )
    scanned_from = django_filters.DateFilter(field_name="scanned_at", lookup_expr="date__gte")
    scanned_to = django_filters.DateFilter(field_name="scanned_at", lookup_expr="date__lte")

    class Meta:
        model = Scan
        fields = ["plant", "is_healthy", "scanned_from", "scanned_to"]

    def filter_is_healthy(self, queryset, name, value):
        return queryset.filter(is_healthy=value == "true")
