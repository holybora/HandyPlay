# :app

Application entry point for HandyPlay.

## Module Info

- **Namespace:** `com.sls.handbook`
- **Application ID:** `com.sls.handbook`
- **Version:** 1.0 (versionCode 1)
- **Plugins:** `handyplay.android.application`, `handyplay.android.hilt`, `kotlin.compose`, `kotlin.serialization`

## Dependencies

- `:core:common`, `:core:ui`, `:core:designsystem`, `:core:domain`, `:core:data`, `:core:model`, `:core:network`
- `:navigation`
- `:feature:welcome`, `:feature:home`
- AndroidX Core, Lifecycle, Activity Compose
- Compose BOM + Material3
- Navigation Compose, Hilt Navigation Compose

## Key Files

- `MainActivity.kt` — `@AndroidEntryPoint` single Activity, enables edge-to-edge, sets content to `HandyPlayApp`
- `HandyPlayApplication.kt` — `@HiltAndroidApp` Application class
- `ui/HandyPlayApp.kt` — Root composable: `HandyPlayTheme` → `Scaffold` → `NavHost` (start: `WelcomeDestination`, routes: `WelcomeDestination`, `HomeDestination`)

## Source

- `src/main/java/com/sls/handbook/`

## Build Commands

```bash
./gradlew :app:assembleDebug
./gradlew :app:testDebugUnitTest
./gradlew :app:connectedAndroidTest
```

## Tests

- `src/test/` — JVM unit tests
- `src/androidTest/` — Compose UI / instrumented tests
