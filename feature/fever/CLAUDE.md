# :feature:fever

Weather screen displaying random location conditions with Travello-inspired design (gradient background, glassmorphism cards, colored stat pills). Features separate FeverTheme to avoid affecting other screens. Implements edge-to-edge display with system inset handling via `statusBarsPadding`, `navigationBarsPadding`, and `systemBarsPadding` modifiers.

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
- `material-icons-extended` — Extended Material icons for stat pill icons (Thermostat, Air, WaterDrop)

## Key Files

- `FeverViewModel.kt` — `@HiltViewModel` with `StateFlow<FeverUiState>`, fetches weather via `WeatherRepository`, maps to `WeatherDisplayData` using `StringResolver`.
- `FeverUiState.kt` — Sealed class with `weatherDisplay` property: `Loading` and `Error` use `WeatherDisplayData.empty()`, `Success` holds real data. All states expose display data so `WeatherContent` is always rendered.
- `WeatherDisplayData.kt` — Presentation model with pre-formatted string fields for UI rendering. `companion object` provides `empty()` factory returning blank strings for loading/error states.
- `WeatherMapper.kt` — `Weather.toDisplayData(StringResolver)` extension mapping domain model to presentation model using i18n string resources
- `StringResolver.kt` — Interface for i18n-safe string resolution + Hilt `@Module` providing `Context`-backed implementation
- `FeverRoute.kt` — Route composable wrapping `FeverScreen` in `FeverTheme` for isolated theming
- `FeverScreen.kt` — Main Travello-inspired composable with sky-blue gradient background, hero section (weather icon + stat pills), glassmorphism cards, weather details grid, FAB refresh. `WeatherContent` is always rendered; sections gated by `AnimatedVisibility` with `fadeIn`/`fadeOut(tween(FadeDurationMs))`. Text values use `AnimatedValueText` helper (`AnimatedContent` with fade). Error state shown via `Snackbar` with retry action. **Implements edge-to-edge display:** `WeatherContent` uses `statusBarsPadding()`; FAB uses `navigationBarsPadding()`. Includes 7 @Preview functions.
- `FeverComponents.kt` — Reusable internal composables: `GlassCard`, `WeatherIconCard` (with `Crossfade` for icon, `AnimatedContent` for temperature), `GlassDetailCard` (with `AnimatedContent` for value). Includes 3 @Preview functions.
- `theme/FeverTheme.kt` — Custom `MaterialTheme` with Travello color scheme and typography; overrides app-wide theme only for Fever
- `theme/FeverColor.kt` — Travello-inspired colors (sky blue gradient, glass white surfaces, orange/blue/teal accents) and `LocalFeverColors` CompositionLocal for extended color access
- `theme/FeverType.kt` — Custom typography with 56sp Light display temperature, adjusted label spacing

## Source

- `src/main/kotlin/com/sls/handbook/feature/fever/` — Feature implementation
- `src/main/kotlin/com/sls/handbook/feature/fever/theme/` — Scoped theme system
- `src/main/res/values/strings.xml` — English string resources
- `src/main/res/values-de/`, `values-es/`, `values-fr/` — German, Spanish, French translations
- `src/test/kotlin/com/sls/handbook/feature/fever/` — Unit tests (WeatherMapperTest)

## Patterns

- **ViewModel + StateFlow:** ViewModel exposes `uiState: StateFlow<FeverUiState>` collected via `collectAsStateWithLifecycle()` in Route
- **Always-rendered WeatherContent:** `WeatherContent` is rendered for all UI states; `FeverUiState` sealed class exposes `weatherDisplay` with `empty()` defaults for `Loading`/`Error`, so transitions are value changes (empty → real data) rather than container visibility toggles
- **Fade animations for data transitions:** `AnimatedVisibility` with `fadeIn`/`fadeOut(tween(FadeDurationMs))` gates sections on data availability; `AnimatedContent` with matching fade spec animates individual text value changes; `Crossfade` animates weather icon swaps. All durations use shared `FadeDurationMs` constant.
- **Route wrapper pattern:** `FeverRoute` wraps `FeverScreen` in `FeverTheme`, ensuring custom theme only applies to this feature
- **CompositionLocal for theme access:** `LocalFeverColors.current` allows nested composables to access extended colors without prop drilling
- **Glassmorphism UI:** Semi-transparent white surfaces with borders and soft shadows simulate glass effect
- **Colored stat pills:** Row of cards with colored circular icon backgrounds showing weather metrics (temperature, wind, humidity)
- **i18n via StringResolver:** `StringResolver` interface abstracts `Context.getString()` for testability; injected into ViewModel via Hilt, passed to `WeatherMapper` for localized formatting
- **Error handling via Snackbar:** Error state triggers a `Snackbar` with retry action using `LaunchedEffect` + `rememberUpdatedState`; keeps the always-rendered WeatherContent pattern intact
- **Edge-to-edge inset handling:**
  - `WeatherContent` column uses `statusBarsPadding()` to avoid overlapping status bar on initial load
  - FAB positioned at bottom with `navigationBarsPadding()` + 24.dp explicit padding to sit above system navigation
  - Imports: `androidx.compose.foundation.layout.{statusBarsPadding, navigationBarsPadding}`

## Notes

- The `FeverTheme` is **intentionally scoped** to the Fever screen via `FeverRoute` wrapper. It does NOT affect other screens (Home, Category, Gallery, TTL Cache), which continue using `HandyPlayTheme` from `:core:designsystem`.
- The theme is light-mode only (sky blue gradient is inherently a light design); does not provide dark mode variants.
- Stat pill icons use `material-icons-extended` (Thermostat, Air, WaterDrop) to avoid bloating core icon sets.
- Do NOT add business logic or state management to composables. Keep all logic in `FeverViewModel`.
- Edge-to-edge display is enabled app-wide via `WindowCompat.setDecorFitsSystemWindows(window, false)` in HandyPlayApp; inset padding is applied per-composable as needed.
