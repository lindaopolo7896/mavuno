from django.contrib import admin

from .models import Scan


@admin.register(Scan)
class ScanAdmin(admin.ModelAdmin):
    list_display = ("user", "plant_name", "disease", "confidence", "is_healthy", "scanned_at")
    list_filter = ("is_healthy", "plant_name")
    search_fields = ("user__email", "plant_name", "disease")
    readonly_fields = ("id", "created_at")
