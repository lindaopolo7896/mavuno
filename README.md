# Mavuno 🌱

Mavuno is a student group project exploring how mobile technology can help farmers monitor crop health. The project aims to let users capture or select plant images, identify possible plant diseases, and access useful information to support crop care.

The Android application is being developed in **Kotlin** with **Jetpack Compose**. The project is still in progress: the current work focuses on the mobile application and its user interface, while backend integration and the connection to the disease-detection workflow are planned as part of the ongoing group development.

> **Project status:** Work in progress — academic group project. The current Android app includes UI screens and sample plant-scan data. It is not yet connected to a backend or a working disease-detection model, so sample scan information should not be interpreted as live predictions.

## Project Goals

Mavuno is being developed with the following goals:

- Make it easier for users to check the health of their crops using a mobile application.
- Provide a workflow for capturing or selecting plant images for analysis.
- Present a result that can distinguish healthy plants from diseased plants and, where supported by the detection model, identify the disease.
- Keep a history of previous scans.
- Provide accessible information about common plant diseases and crop care.

These describe the intended direction of the project; not all functionality is implemented yet.

## Current Android Application

The current Android client contains the following UI work:

- **Authentication screens:** splash, sign-up, login, email verification, forgot-password, and reset-password screens.
- **Home dashboard:** crop-health introduction and a recent-scans section populated with local sample data.
- **Scan history:** sample scan records with healthy/diseased filters.
- **Plant detail route:** an initial screen for an individual scan result.
- **Navigation and reusable components:** bottom navigation, dashboard layout, input fields, buttons, scan cards, and settings components.
- **Theme:** a crop-focused colour system and Material 3 styling.

Some screens are placeholders or are still being implemented. The scan flow, live predictions, and backend-powered data are not complete.

## Technology Stack

| Area | Technologies |
| --- | --- |
| Language | Kotlin |
| Android UI | Jetpack Compose |
| Design system | Material 3 |
| Navigation | Navigation Compose |
| Camera foundation | CameraX |
| Android libraries | AndroidX Core KTX, Activity Compose, Lifecycle |
| Build system | Gradle with Kotlin DSL and version catalog |
| IDE | Android Studio |

## Repository Structure

```text
mavuno/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/example/mavuno/
│   │       │   ├── data/          # Plant models and sample scan data
│   │       │   ├── ui/
│   │       │   │   ├── components/ # Reusable Compose components
│   │       │   │   ├── layouts/    # Shared screen layouts
│   │       │   │   ├── navigation/ # App navigation
│   │       │   │   ├── screens/    # Authentication and dashboard screens
│   │       │   │   └── theme/      # App colours, typography and theme
│   │       │   └── MainActivity.kt
│   │       └── res/                # Android resources and image assets
│   └── build.gradle.kts
├── gradle/
│   └── libs.versions.toml
├── build.gradle.kts
└── settings.gradle.kts
```

## Getting Started

### Requirements

- Android Studio
- Android SDK matching the project's configured compile SDK (API 37)
- JDK 11-compatible toolchain
- An Android emulator or physical Android device

### Run the application

1. Clone the repository:

   ```bash
   git clone https://github.com/lindaopolo7896/mavuno.git
   cd mavuno
   ```

2. Open the project directory in Android Studio.
3. Allow Android Studio to sync the Gradle project and download dependencies.
4. Select an emulator or connect an Android device.
5. Run the `app` configuration.

To build a debug APK from the project root:

```bash
./gradlew assembleDebug
```

On Windows, use:

```powershell
.\gradlew.bat assembleDebug
```

The debug APK is generated under `app/build/outputs/apk/debug/`.

## Planned Work

The group is continuing development. Areas planned for the next stages include:

- Completing the plant image capture and submission flow.
- Integrating the backend service with the Android application.
- Connecting the disease-detection model and displaying its results.
- Persisting user accounts and scan history through the backend.
- Completing disease information and recommendations.
- Testing the end-to-end application workflow.

## Academic Project

Mavuno is being developed collaboratively as part of a university project. The repository reflects the Android application work currently in progress; backend and model integration are part of the wider team project.

## License

No license has currently been specified for this repository. All rights are reserved unless a license is added.
