# Mavuno 🌱

Mavuno is an Android-based crop health application that helps users identify plant diseases from leaf images. Users can create an account, scan or upload a plant leaf, view an AI-generated diagnosis, and keep track of previous scans.

The project brings together a native Android application, a Django REST API, and a deep-learning image classification model trained on the PlantVillage dataset.

## Features

- **User accounts:** Register, log in, refresh authentication sessions, update profile details, and delete an account.
- **Leaf scanning:** Capture or select a leaf image and submit it for analysis.
- **AI-powered diagnosis:** Identify the plant and predict whether it is healthy or affected by a disease.
- **Prediction results:** Display the predicted disease and confidence, together with the model's top predictions.
- **Scan history:** View previous scans, open individual results, filter healthy or diseased scans, search records, and delete scans.
- **Crop health information:** Support users in understanding plant health and common crop diseases.

## How it works

1. A user signs in to the Mavuno Android application.
2. The user captures or selects an image of a plant leaf.
3. The Android app uploads the image to the backend API.
4. The backend preprocesses the image and runs inference using the trained ResNet18 model.
5. The API returns the diagnosis and top predictions, and saves the scan to the authenticated user's history.
6. The user can revisit or manage saved scan results in the application.

The model recognises 38 PlantVillage disease/healthy classes, as well as a separate `Not_a_leaf` class used to reject images that are not plant leaves. The backend also applies confidence and image-similarity checks before saving a prediction.

## Technology Stack

| Component | Technologies |
| --- | --- |
| Android application | Kotlin, Jetpack Compose, Material 3 |
| Android navigation | Navigation Compose |
| Camera | CameraX |
| Networking | Retrofit, OkHttp |
| Android architecture support | ViewModel, Kotlin Coroutines |
| Image loading | Coil |
| Backend | Python, Django, Django REST Framework |
| Authentication | JSON Web Tokens (JWT) |
| Machine learning | PyTorch, TorchVision, ResNet18 |
| Dataset | PlantVillage |
| Database | SQLite by default; PostgreSQL supported |
| Backend dependencies | uv |
| Android build | Gradle with Kotlin DSL |

## Repository Structure

```text
mavuno/
├── frontend/                  # Native Android application
│   ├── app/
│   │   ├── src/main/
│   │   │   ├── java/          # Kotlin source code
│   │   │   └── res/           # Android resources
│   │   └── build.gradle.kts
│   ├── gradle/
│   ├── build.gradle.kts
│   └── settings.gradle.kts
│
├── backend/                   # Django REST API and AI inference
│   ├── apps/
│   │   ├── users/             # User account model and migrations
│   │   └── scans/             # Scan records and migrations
│   ├── api/v1/
│   │   ├── users/             # Authentication and profile endpoints
│   │   └── scans/             # Scan and prediction endpoints
│   ├── config/                # Django configuration
│   ├── services/
│   │   └── ai/                # Model loading, preprocessing and inference
│   ├── ml_models/             # Model files (downloaded separately)
│   ├── api.md                 # API reference
│   ├── manage.py
│   ├── pyproject.toml
│   └── .env.example
│
└── README.md
```

## Getting Started

The Android app and backend are separate projects. Set up and run the backend before connecting the Android app to it.

### Prerequisites

- Android Studio and Android SDK (the project is configured with compile SDK 37)
- An Android emulator or physical Android device
- Python 3.10 or later
- [uv](https://docs.astral.sh/uv/) for Python dependency management
- Git

### 1. Clone the repository

```bash
git clone https://github.com/lindaopolo7896/mavuno.git
cd mavuno
```

### 2. Set up the backend

```bash
cd backend
uv sync
cp .env.example .env
```

Open `.env` and set a secure Django `SECRET_KEY`. You can generate one with:

```bash
uv run python -c "from django.core.management.utils import get_random_secret_key as g; print(g())"
```

Apply database migrations and create an administrator account:

```bash
uv run python manage.py migrate
uv run python manage.py createsuperuser
```

Start the development server:

```bash
uv run python manage.py runserver 0.0.0.0:8000
```

The API will be available at `http://127.0.0.1:8000/api/v1/` on the host machine.

#### Machine-learning model files

The trained model weights are not stored in this Git repository. Obtain the model files produced by the training workflow and place them in `backend/ml_models/`:

- `resnet18_v2.pt` — trained ResNet18 weights.
- `resnet18_v2_check.pt` — class-average and similarity-check data.

The backend's AI configuration points to these filenames by default. If the files are missing, model-based diagnosis will not be available. See [backend/README.md](backend/README.md) for model details and configuration.

### 3. Set up the Android application

1. Open Android Studio.
2. Choose **Open** and select the repository's `frontend/` directory.
3. Allow Gradle to sync and download the required dependencies.
4. Start an Android emulator or connect a device.
5. Run the `app` configuration.

To build a debug APK from the `frontend/` directory:

```bash
./gradlew assembleDebug
```

On Windows:

```powershell
.\gradlew.bat assembleDebug
```

The APK is generated at `frontend/app/build/outputs/apk/debug/app-debug.apk`.

### 4. Connect the Android app to the backend

For an Android emulator, the host machine's localhost is reachable using `10.0.2.2`. The API documentation uses this local base URL:

```text
http://10.0.2.2:8000/api/v1/
```

For a physical Android device, use the host computer's reachable local network IP address instead. Ensure the device and computer can communicate and configure the app's API base URL for the environment you are running.

## API Documentation

The backend exposes a versioned REST API under `/api/v1/`.

The API supports:

- Registration, login, token refresh, and logout.
- Retrieving, updating, and deleting the authenticated user's profile.
- Submitting a leaf image for AI diagnosis.
- Creating, listing, filtering, retrieving, and deleting scan records.

See the detailed [API v1 documentation](backend/api.md) for endpoints, request formats, response examples, and error codes.

## Model and Dataset

Mavuno uses a ResNet18 image classification model trained using the PlantVillage dataset. The model supports 38 plant disease/healthy categories across 14 crops, with an additional `Not_a_leaf` category for rejecting non-leaf images.

The backend performs image preprocessing before inference and can return the top three predictions. Predictions that fail the configured image-recognition or confidence checks are rejected rather than saved as completed scans.

## Project Status

The Mavuno application, backend API, and model-inference workflow have been developed as a completed university group project. The model weight files are maintained separately from this repository and must be supplied locally to run AI diagnosis.

## Contributors

Mavuno was developed collaboratively as a university group project.

## License

No license has currently been specified for this repository. All rights are reserved unless a license is added.
