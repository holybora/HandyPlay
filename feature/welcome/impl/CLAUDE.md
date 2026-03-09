# :feature:welcome

Welcome/onboarding screen — the app's start destination.

## Module Info

- **Namespace:** `com.sls.handbook.feature.welcome`
- **Type:** Feature module
- **Plugins:** `handyplay.android.feature.impl`, `handyplay.android.roborazzi`

## Auto-included by `handyplay.android.feature.impl`

- Compose + Hilt + Lifecycle + Navigation
- `:core:ui`, `:core:designsystem`, `:core:domain`, `:core:model`, `:core:navigation`

## Key Files

- `WelcomeScreen.kt` — Main welcome UI composable
- `WelcomeRoute.kt` — Navigation wrapper (called from NavHost)

## Source

- `src/main/java/com/sls/handbook/feature/welcome/`
- `src/test/kotlin/com/sls/handbook/feature/welcome/WelcomeScreenScreenshotTest.kt` — Roborazzi screenshot tests (light, dark)
