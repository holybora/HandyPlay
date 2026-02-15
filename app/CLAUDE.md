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
- `:feature:welcome`, `:feature:home`, `:feature:category`, `:feature:ttlcache`
- AndroidX Core, Lifecycle, Activity Compose
- Compose BOM + Material3
- Navigation Compose, Hilt Navigation Compose

## Key Files

- `MainActivity.kt` — `@AndroidEntryPoint` single Activity, enables edge-to-edge, sets content to `HandyPlayApp`
- `HandyPlayApplication.kt` — `@HiltAndroidApp` Application class
- `ui/HandyPlayApp.kt` — Root composable with `BottomSearchBarViewModel`, manages search state and destination-based visibility
- `ui/HandyPlayNavHost.kt` — NavHost routes: `WelcomeDestination` → `HomeDestination` (pops Welcome), `HomeDestination` → `CategoryDestination`, `CategoryDestination` → `TtlCacheDestination` via `Topic.ID_TTL_CACHE`
- `ui/BottomSearchBarViewModel.kt` — `@HiltViewModel` managing search query, breadcrumb segments, bar visibility, and navigation events via Channel

## Source

- `src/main/java/com/sls/handbook/`

## Build Commands

```bash
./gradlew :app:assembleDebug
./gradlew :app:testDebugUnitTest
./gradlew :app:connectedAndroidTest
```

## Tests

- `src/test/` — JVM unit tests, including `BottomSearchBarViewModelTest.kt`
- `src/androidTest/` — Compose UI / instrumented tests
