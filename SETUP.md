# 🚀 Raitha Vartha - Local Setup Guide

## Prerequisites

- Android Studio (Ladybug or newer)
- JDK 17 or higher
- Git
- Firebase project with Phone Authentication enabled
- Google Gemini API Key
- Groq API Key

## Quick Start

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/Raitha_Vartha.git
cd Raitha_Vartha
```

### 2. Configure Local Properties

Create/Update `local.properties` with your API keys:

```properties
sdk.dir=path/to/android/sdk
GEMINI_API_KEY=your_gemini_api_key_here
GROQ_API_KEY=your_groq_api_key_here
```

⚠️ **IMPORTANT:** Never commit `local.properties` with API keys. It's in `.gitignore` for your protection.

### 3. Configure Firebase

1. Download your Firebase `google-services.json` from Firebase Console
2. Copy it to the `app/` directory:

```bash
cp ~/Downloads/google-services.json app/
```

⚠️ **IMPORTANT:** `google-services.json` is in `.gitignore` for security. Each developer needs their own copy.

### 4. Verify Setup

```bash
# Run a quick build to ensure all dependencies are resolved
./gradlew clean build
```

### 5. Open in Android Studio

1. Open Android Studio
2. Select **File** → **Open**
3. Navigate to and select the `Raitha_Vartha` folder
4. Wait for Gradle sync to complete
5. Build → Make Project

## Obtaining API Keys

### Google Gemini API

1. Go to [Google AI Studio](https://aistudio.google.com)
2. Create a new API key for Android
3. Copy and paste into `local.properties` as `GEMINI_API_KEY`

### Groq API

1. Visit [Groq Cloud](https://console.groq.com)
2. Create an API key
3. Copy and paste into `local.properties` as `GROQ_API_KEY`

### Firebase Configuration

1. Go to [Firebase Console](https://console.firebase.google.com)
2. Create a new project or select existing one
3. Add Android app with package name: `com.raithavarta`
4. Download `google-services.json`
5. Copy to `app/` directory

## Project Structure

```
Raitha_Vartha/
├── app/                          # Main Android app module
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/raithavarta/   # Kotlin source code
│   │   │   └── res/                     # Resources
│   │   ├── test/                        # Unit tests
│   │   └── androidTest/                 # Instrumentation tests
│   ├── build.gradle.kts          # App-level build config
│   └── google-services.json      # Firebase config (⚠️ not committed)
├── gradle/                       # Gradle wrapper
├── build.gradle.kts              # Project-level build config
├── settings.gradle.kts           # Gradle settings
├── local.properties              # Local configuration (⚠️ not committed)
├── README.md                     # Project documentation
├── CONTRIBUTING.md               # Contribution guidelines
└── LICENSE                       # MIT License
```

## Building & Testing

### Build Debug APK

```bash
./gradlew assembleDebug
```

### Build Release APK

```bash
./gradlew assembleRelease
```

### Run Unit Tests

```bash
./gradlew test
```

### Run Instrumentation Tests

```bash
./gradlew connectedAndroidTest
```

## Troubleshooting

### Gradle Sync Issues

- Clear Gradle cache: `./gradlew clean`
- Invalidate Android Studio cache: **File** → **Invalidate Caches**

### Firebase Configuration Error

- Ensure `google-services.json` is in the `app/` directory
- Check that package name matches: `com.raithavarta`

### API Key Errors

- Verify `GEMINI_API_KEY` and `GROQ_API_KEY` are set in `local.properties`
- Ensure keys have appropriate permissions in their respective consoles

## Security Notes

⚠️ **NEVER commit these files:**

- `local.properties` (contains API keys)
- `app/google-services.json` (contains Firebase credentials)
- `.gradle/` directory
- `build/` directory

These are already in `.gitignore`. If you accidentally commit them, consider them compromised and regenerate the keys immediately.

## Next Steps

1. Read [CONTRIBUTING.md](CONTRIBUTING.md) for contribution guidelines
2. Check the [README.md](README.md) for feature documentation
3. Join our community discussions on GitHub

---

**Happy Coding! 🌾**
