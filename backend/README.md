# Mavuno Backend

Django + Django REST Framework API for the Mavuno mobile app. A leaf photo is diagnosed by a
ResNet18 model and each result is saved to the user's scan history.

- Auth: JWT (access + refresh tokens)
- AI: ResNet18 trained on PlantVillage (38 classes, 14 crops), run on CPU with PyTorch
- Dependencies: [uv](https://docs.astral.sh/uv/)
- API reference: [api.md](api.md)

## Project layout

```
backend/
├── apps/              # Django apps (models, admin, migrations)
│   ├── users/         # Custom User (email login)
│   └── scans/         # Scan results; `predict` management command
├── config/            # settings, root urls, wsgi/asgi
├── api/
│   └── v1/            # versioned API, grouped like the apps
│       ├── users/     # auth + profile views, serializers, urls
│       └── scans/     # scan + predict views, serializers, urls
├── services/          # business logic used by the views
│   └── ai/            # model loading, preprocessing, class list
├── ml_models/         # resnet18_final.pt (not tracked in git)
├── manage.py
├── pyproject.toml / uv.lock
└── .env.example
```

New API versions go in `api/v2/`, `api/v3/`, ... with the same structure, registered in `api/urls.py`.

## Setup

```bash
cd backend
uv sync
cp .env.example .env
uv run python manage.py migrate
uv run python manage.py createsuperuser
uv run python manage.py runserver 0.0.0.0:8000
```

`SECRET_KEY` in `.env` must be set. A key can be generated with:

```bash
uv run python -c "from django.core.management.utils import get_random_secret_key as g; print(g())"
```

## AI model

| File | Purpose |
|---|---|
| `ml_models/resnet18_final.pt` | Trained weights (state dict, ~45 MB) from `ResNet18_Base.ipynb` |
| `services/ai/classes.py` | The 38 classes, in the order the model outputs them |
| `services/ai/predictor.py` | Loads the model once per process, preprocesses photos, runs inference |

Preprocessing matches `eval_transform` from training: resize to 224×224, then ImageNet
normalisation. PyTorch is installed CPU-only (`[tool.uv.sources]` in `pyproject.toml`).

Predictions below `AI_CONFIDENCE_THRESHOLD` are rejected with `422` and not saved. The model
always returns one of its 38 classes, so the threshold is what filters out unrelated photos.

Running the model on image files directly:

```bash
uv run python manage.py predict path/to/leaf.jpg
```

## Configuration (.env)

| Variable | Default | Notes |
|---|---|---|
| `SECRET_KEY` | required | |
| `DEBUG` | `False` | |
| `ALLOWED_HOSTS` | `localhost,127.0.0.1` | Comma separated |
| `CSRF_TRUSTED_ORIGINS` | empty | Required for `/admin/` login over HTTPS, e.g. `https://api.example.com` |
| `DATABASE_URL` | `sqlite:///db.sqlite3` | Postgres: `postgres://user:pass@host:5432/mavuno` |
| `TIME_ZONE` | `UTC` | |
| `MEDIA_URL` / `MEDIA_ROOT` | `/media/` / `backend/media` | Scan photo storage |
| `SERVE_MEDIA` | `False` | `True`: Django serves `MEDIA_ROOT`. Always on when `DEBUG=True` |
| `PAGE_SIZE` | `20` | Scan history page size |
| `JWT_ACCESS_TOKEN_MINUTES` | `30` | |
| `JWT_REFRESH_TOKEN_DAYS` | `30` | |
| `AUTH_THROTTLE_RATE` | `10/min` | Login/register attempts per IP |
| `SECURE_SSL_REDIRECT` | `True` | When `DEBUG=False` |
| `SECURE_HSTS_SECONDS` | `31536000` | When `DEBUG=False` |
| `SECURE_HSTS_INCLUDE_SUBDOMAINS` | `True` | When `DEBUG=False` |
| `LOG_LEVEL` | `INFO` | |
| `AI_MODEL_PATH` | `ml_models/resnet18_final.pt` | Relative to `backend/` |
| `AI_CONFIDENCE_THRESHOLD` | `0.8` | 0–1 |
| `AI_TOP_K` | `3` | Predictions returned and stored per scan |
| `AI_MAX_UPLOAD_MB` | `10` | Max photo size |
