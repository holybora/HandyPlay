# :app

Application entry point for HandyPlay.

## Module Info

- **Namespace:** `com.sls.handbook`
- **Application ID:** `com.sls.handbook`
- **Version:** 1.0 (versionCode 1)
- **Plugins:** `handyplay.android.application`, `handyplay.android.hilt`, `kotlin.compose`, `kotlin.serialization`

## Dependencies

- All `:core:*` modules, `:navigation`, all `:feature:*` modules
- AndroidX Core, Lifecycle, Activity Compose
- Compose BOM + Material3
- Navigation Compose, Hilt Navigation Compose

## Key Files

- `MainActivity.kt` — `@AndroidEntryPoint` single Activity, sets up edge-to-edge + `HandyPlayApp`
- `HandyPlayApplication.kt` — `@HiltAndroidApp` Application class
- `ui/HandyPlayApp.kt` — Root composable with `NavHost` (start: `WelcomeDestination`)

## Build Commands

```bash
./gradlew :app:assembleDebug
./gradlew :app:testDebugUnitTest
./gradlew :app:connectedAndroidTest
```

## Tests

- `src/test/` — JVM unit tests
- `src/androidTest/` — Compose UI / instrumented tests
