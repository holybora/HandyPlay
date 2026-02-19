# :feature:fever

Weather screen displaying random location conditions with Travello-inspired design (gradient background, glassmorphism cards, colored stat pills). Features separate FeverTheme to avoid affecting other screens. Implements edge-to-edge display with system inset handling via `statusBarsPadding`, `navigationBarsPadding`, and `systemBarsPadding` modifiers. Fully localized (EN, DE, ES, FR) via `stringResource()` and `StringResolver`.

## Module Info

- **Namespace:** `com.sls.handbook.feature.fever`
- **Type:** Feature module
- **Plugin:** `handyplay.android.feature`

## Auto-included by `handyplay.android.feature`

- Compose + Hilt + Lifecycle + Navigation
- `:core:ui`, `:core:designsystem`, `:core:domain`, `:core:model`, `:navigation`

## Dependencies

- `coil.compose` — Image loading for weather icons
- `coil.network.okhttp` — OkHttp integration for Coil
- `material-icons-extended` — Extended Material icons for stat pill icons (Thermostat, Air, WaterDrop) and directional arrows (ArrowBack, ArrowForward)

## Key Files

- `FeverViewModel.kt` — `@HiltViewModel` with `StateFlow<FeverUiState>`, injects `StringResolver` and `WeatherRepository`, maps weather to `WeatherDisplayData`
- `FeverUiState.kt` — Sealed interface: `Loading`, `Success(weatherDisplay)`, `Error(message)`
- `WeatherDisplayData.kt` — Presentation model with pre-formatted string fields for UI rendering
- `WeatherMapper.kt` — `Weather.toDisplayData(StringResolver)` extension mapping domain model to presentation model using string resources
- `StringResolver.kt` — Interface + Hilt `@Module` for resolving Android string resources in non-Context classes (ViewModel, Mapper)
- `FeverRoute.kt` — Route composable wrapping `FeverScreen` in `FeverTheme` for isolated theming
- `FeverScreen.kt` — Main Travello-inspired composable with sky-blue gradient background, hero section (weather icon + stat pills), glassmorphism cards, weather details grid, dual `SwipeHintFab` buttons (left arrow at BottomStart, right arrow at BottomEnd) triggering refresh. **Implements edge-to-edge display:** `WeatherContent` uses `statusBarsPadding()`; `ErrorContent` uses `systemBarsPadding()`; FABs use `navigationBarsPadding()`. All UI labels use `stringResource()`. Includes 7 @Preview functions.
- `FeverComponents.kt` — Reusable internal composables: `GlassCard`, `WeatherIconCard`, `GlassDetailCard`. Includes 3 @Preview functions.
- `theme/FeverTheme.kt` — Custom `MaterialTheme` with Travello color scheme and typography; overrides app-wide theme only for Fever
- `theme/FeverColor.kt` — Travello-inspired colors (sky blue gradient, glass white surfaces, orange/blue/teal accents) and `LocalFeverColors` CompositionLocal for extended color access
- `theme/FeverType.kt` — Custom typography with 56sp Light display temperature, adjusted label spacing

## Resources

- `src/main/res/values/strings.xml` — Default (English) string resources
- `src/main/res/values-de/strings.xml` — German translations
- `src/main/res/values-es/strings.xml` — Spanish translations
- `src/main/res/values-fr/strings.xml` — French translations

## Source

- `src/main/kotlin/com/sls/handbook/feature/fever/` — Feature implementation
- `src/main/kotlin/com/sls/handbook/feature/fever/theme/` — Scoped theme system
- `src/test/kotlin/com/sls/handbook/feature/fever/` — Unit tests (WeatherMapperTest)

## Patterns

- **ViewModel + StateFlow:** ViewModel exposes `uiState: StateFlow<FeverUiState>` collected via `collectAsStateWithLifecycle()` in Route
- **Route wrapper pattern:** `FeverRoute` wraps `FeverScreen` in `FeverTheme`, ensuring custom theme only applies to this feature
- **CompositionLocal for theme access:** `LocalFeverColors.current` allows nested composables to access extended colors without prop drilling
- **StringResolver for i18n in non-Context classes:** `StringResolver` interface injected via Hilt into ViewModel/Mapper to resolve `R.string.*` resources without direct `Context` dependency
- **Glassmorphism UI:** Semi-transparent white surfaces with borders and soft shadows simulate glass effect
- **Colored stat pills:** Row of cards with colored circular icon backgrounds showing weather metrics (temperature, wind, humidity)
- **Dual SwipeHintFab pattern:** Two FABs with directional arrow icons (ArrowForward on left, ArrowBack on right) hinting at swipe gestures, both triggering refresh
- **Edge-to-edge inset handling:**
  - `WeatherContent` column uses `statusBarsPadding()` to avoid overlapping status bar on initial load
  - `ErrorContent` uses `systemBarsPadding()` for full system bar inset (status + navigation)
  - FABs positioned at bottom with `navigationBarsPadding()` + 24.dp explicit padding to sit above system navigation
  - Imports: `androidx.compose.foundation.layout.{statusBarsPadding, navigationBarsPadding, systemBarsPadding}`

## Notes

- The `FeverTheme` is **intentionally scoped** to the Fever screen via `FeverRoute` wrapper. It does NOT affect other screens (Home, Category, Gallery, TTL Cache), which continue using `HandyPlayTheme` from `:core:designsystem`.
- The theme is light-mode only (sky blue gradient is inherently a light design); does not provide dark mode variants.
- Stat pill icons use `material-icons-extended` (Thermostat, Air, WaterDrop) to avoid bloating core icon sets.
- Do NOT add business logic or state management to composables. Keep all logic in `FeverViewModel`.
- Edge-to-edge display is enabled app-wide via `WindowCompat.setDecorFitsSystemWindows(window, false)` in HandyPlayApp; inset padding is applied per-composable as needed.
- All user-visible strings must use `stringResource()` in composables or `StringResolver` in ViewModel/Mapper for i18n support. Do NOT hardcode strings.
