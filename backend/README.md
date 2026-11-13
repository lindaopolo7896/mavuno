# Mavuno Backend

Django + Django REST Framework API for the Mavuno mobile app (plant disease scanning).
Auth uses JWT (access + refresh tokens). Dependencies are managed with [uv](https://docs.astral.sh/uv/).

## Project layout

```
backend/
├── apps/           # Django apps (models, admin, migrations)
│   ├── users/      # Custom User (email login)
│   └── scans/      # Scan results
├── config/         # settings, root urls, wsgi/asgi
├── api/
│   └── v1/         # versioned API, grouped like the apps
│       ├── users/  # auth + profile views, serializers, urls
│       └── scans/  # scan history views, serializers, urls
├── services/       # business logic used by the views
├── manage.py
├── pyproject.toml / uv.lock
└── .env.example
```

To add `v2`, create `api/v2/` with the same structure and register it in `api/urls.py`.

## Setup

```bash
cd backend
uv sync                      # creates .venv and installs dependencies
cp .env.example .env         # then set SECRET_KEY (command is in the file)
uv run python manage.py migrate
uv run python manage.py createsuperuser   # optional, for /admin/
uv run python manage.py runserver 0.0.0.0:8000
```

The Android emulator reaches your computer at `http://10.0.2.2:8000`. On a physical phone,
use your computer's LAN IP and add it to `ALLOWED_HOSTS` in `.env`.

## Configuration (.env)

| Variable | Default | Notes |
|---|---|---|
| `SECRET_KEY` | — (required) | |
| `DEBUG` | `False` | Media files are only served by Django when `True` |
| `ALLOWED_HOSTS` | `localhost,127.0.0.1` | Comma separated |
| `CSRF_TRUSTED_ORIGINS` | empty | Only needed for the admin behind a domain |
| `DATABASE_URL` | `sqlite:///db.sqlite3` | For Postgres: `postgres://user:pass@host:5432/mavuno` (also run `uv add "psycopg[binary]"`) |
| `TIME_ZONE` | `UTC` | |
| `MEDIA_URL` / `MEDIA_ROOT` | `/media/` / `backend/media` | Where scan images are stored |
| `PAGE_SIZE` | `20` | Scan history page size |
| `JWT_ACCESS_TOKEN_MINUTES` | `30` | |
| `JWT_REFRESH_TOKEN_DAYS` | `30` | |

## API (v1)

All endpoints except register, login and refresh need `Authorization: Bearer <access token>`.

| Method | Path | Body | Response |
|---|---|---|---|
| POST | `/api/v1/auth/register/` | `email`, `password`, `full_name`?, `phone_number`? | `201` `{user, access, refresh}` |
| POST | `/api/v1/auth/login/` | `email`, `password` | `200` `{user, access, refresh}` |
| POST | `/api/v1/auth/token/refresh/` | `refresh` | `200` `{access, refresh}` (new refresh token each time) |
| POST | `/api/v1/auth/logout/` | `refresh` | `204` (refresh token is blacklisted) |
| GET | `/api/v1/users/me/` | | `200` profile |
| PATCH | `/api/v1/users/me/` | `full_name`, `phone_number` | `200` profile |
| DELETE | `/api/v1/users/me/` | | `204` account and all scans deleted |
| POST | `/api/v1/scans/` | see below | `201` scan |
| GET | `/api/v1/scans/?page=1` | | `200` `{count, next, previous, results}`, newest first |
| GET | `/api/v1/scans/<id>/` | | `200` scan |
| DELETE | `/api/v1/scans/<id>/` | | `204` |

### Scan fields

All fields are optional when saving a scan.

| Field | Type | Notes |
|---|---|---|
| `image` | file | Send as `multipart/form-data` |
| `plant_name` | string | |
| `disease` | string | Top predicted disease |
| `confidence` | float 0–1 | Confidence of the top prediction |
| `is_healthy` | bool | |
| `predictions` | list | `[{"label": "Leaf rust", "confidence": 0.91}, ...]`. In multipart requests, send it as a JSON string |
| `recommendation` | string | Treatment / care advice |
| `scanned_at` | ISO datetime | When the photo was taken; defaults to now |

Read-only: `id` (UUID), `created_at`.

Example (JSON, no image):

```bash
curl -X POST http://localhost:8000/api/v1/scans/ \
  -H "Authorization: Bearer $ACCESS" -H "Content-Type: application/json" \
  -d '{"plant_name":"Maize","disease":"Northern leaf blight","confidence":0.93,
       "predictions":[{"label":"Northern leaf blight","confidence":0.93}]}'
```
