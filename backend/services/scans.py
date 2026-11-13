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
