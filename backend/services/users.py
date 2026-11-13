"""Business logic for user accounts."""
from django.db import transaction
from rest_framework_simplejwt.exceptions import TokenError
from rest_framework_simplejwt.tokens import RefreshToken

from apps.users.models import User

from .scans import delete_scan_images


def register_user(*, email, password, **profile):
    return User.objects.create_user(email=email, password=password, **profile)


def issue_tokens(user):
    refresh = RefreshToken.for_user(user)
    return {"refresh": str(refresh), "access": str(refresh.access_token)}


def logout_user(refresh_token):
    """Blacklist the refresh token so it can't be used again. Returns False if the token is invalid."""
    try:
        RefreshToken(refresh_token).blacklist()
    except TokenError:
        return False
    return True


@transaction.atomic
def delete_user(user):
    """Delete the account, its scans, and their image files."""
    delete_scan_images(user.scans.all())
    user.delete()
