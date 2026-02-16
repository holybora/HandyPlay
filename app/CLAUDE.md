# :app

Application entry point for HandyPlay. Single-activity Compose app with type-safe navigation and Hilt DI. Includes debug-only E2E test recording infrastructure.

## Module Info

- **Namespace:** `com.sls.handbook`
- **Application ID:** `com.sls.handbook`
- **Version:** 1.0 (versionCode 1)
- **Plugins:** `handyplay.android.application`, `handyplay.android.hilt`, `kotlin.compose`, `kotlin.serialization`

## Dependencies

- `:core:common`, `:core:ui`, `:core:designsystem`, `:core:domain`, `:core:data`, `:core:model`, `:core:network`
- `:navigation`
- `:feature:welcome`, `:feature:home`, `:feature:category`, `:feature:ttlcache`
- AndroidX Core, Lifecycle, Activity Compose
- Compose BOM + Material3
- Navigation Compose, Hilt Navigation Compose

## Key Files

### Main

- `MainActivity.kt` — `@AndroidEntryPoint` single Activity, enables edge-to-edge, sets content to `HandyPlayApp`
- `HandyPlayApplication.kt` — `@HiltAndroidApp` Application class
- `ui/HandyPlayApp.kt` — Root composable with `BottomSearchBarViewModel`, manages search state and destination-based visibility
- `ui/HandyPlayNavHost.kt` — NavHost routes: `WelcomeDestination` → `HomeDestination` (pops Welcome), `HomeDestination` → `CategoryDestination`, `CategoryDestination` → `TtlCacheDestination` via `Topic.ID_TTL_CACHE`
- `ui/BottomSearchBarViewModel.kt` — `@HiltViewModel` managing search query, breadcrumb segments, bar visibility, and navigation events via Channel

### Debug-only: E2E Test Recording (src/debug/)

- `recording/RecordingActivity.kt` — Debug Activity that wraps `HandyPlayApp()` with `RecordingOverlay`; launches via intent action `com.sls.handbook.E2E_RECORD`
- `recording/RecordingOverlay.kt` — Composable overlay: intercepts touch events via `pointerInput(PointerEventPass.Initial)`, classifies taps/swipes, shows draggable red stop button, pulsing recording indicator
- `recording/RecordingController.kt` — Thread-safe event collector with relative timestamps
- `recording/RecordedEvent.kt` — Event data classes (TAP, SWIPE, BACK_PRESS, TEXT_INPUT) and session metadata; serializes to JSON via `org.json`

## Source

- `src/main/java/com/sls/handbook/` — Production code (single Activity + navigation)
- `src/debug/java/com/sls/handbook/recording/` — Debug-only recording infrastructure (debug builds only)

## Build Commands

```bash
./gradlew :app:assembleDebug
./gradlew :app:assembleRelease
./gradlew :app:testDebugUnitTest
./gradlew :app:connectedAndroidTest
./gradlew :app:installAndRun  # Installs debug APK and launches MainActivity
```

## Tests
- `src/test/` — JVM unit tests
  - `BottomSearchBarViewModelTest.kt` — ViewModel state management tests
  - `BottomSearchBarModelsTest.kt` — `BottomSearchBarUiState`, `CurrentScreen`, navigation events tests
- `src/androidTest/` — Compose UI / instrumented tests


## Notes

- **Debug source set isolation**: All E2E recording code is in `src/debug/` and is compiled only for debug builds. Release builds have zero overhead.
- **Recording workflow**: `/record-e2e` skill builds debug APK, launches `RecordingActivity` via custom intent, user interacts, taps red button to stop → JSON saved to `filesDir`, pulled via ADB, saved as test in `e2e-tests/<name>/`. `/run-e2e` skill replays tests.
- **Touch interception without consumption**: `PointerEventPass.Initial` lets the overlay observe all touches without consuming them — the underlying UI works normally.
