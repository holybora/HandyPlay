# :feature:fever

Weather screen displaying random location conditions with current weather, 5-day daily forecast, and hourly forecast cards. Travello-inspired design with gradient background, glassmorphism cards, colored stat pills. Features separate FeverTheme, edge-to-edge display, and full i18n localization (EN, DE, ES, FR).

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

- `FeverViewModel.kt` — `@HiltViewModel` with `StateFlow<FeverUiState>`, fetches current weather, 5-day daily forecast, and hourly forecast via `WeatherRepository`, maps all to `WeatherDisplayData` using `StringResolver`.
- `FeverUiState.kt` — Sealed class: `Loading`, `Error`, `Success`. All expose `weatherDisplay: WeatherDisplayData` so `WeatherContent` is always rendered (using empty defaults for Loading/Error).
- `WeatherDisplayData.kt` — Presentation model with pre-formatted fields for current weather, `forecast: List<DailyForecastDisplayData>`, and `hourlyForecasts: List<HourlyDisplayData>`. Factory: `empty()`.
- `HourlyDisplayData.kt` — Data class with formatted hourly fields: `timeText`, `iconUrl`, `temperatureText`, `popText` (precipitation %).
- `WeatherMapper.kt` — Extension functions: `Weather.toDisplayData()`, `DailyForecast.toDisplayData()`, `HourlyForecast.toHourlyDisplayData()`. All use `StringResolver` for i18n.
- `StringResolver.kt` — Interface for i18n string resolution + Hilt `@Module` providing `Context`-backed impl.
- `FeverRoute.kt` — Route composable wrapping `FeverScreen` in `FeverTheme` for scoped theming.
- `FeverScreen.kt` — Main Travello-inspired composable: sky-blue gradient bg, hero section (icon + stat pills), current weather details, 5-day forecast card, horizontally scrollable hourly forecast card (showing time/icon/temp/pop%), dual `SwipeHintFab` buttons (ArrowBack/ArrowForward at bottom). Sections gated by `AnimatedVisibility` with fade. Edge-to-edge: `statusBarsPadding()` on content, `navigationBarsPadding()` on FABs. All labels via `stringResource()`.
- `FeverComponents.kt` — Reusable composables: `GlassCard`, `WeatherIconCard`, `GlassDetailCard`, `HourlyForecastSection` (LazyRow), `HourlyForecastItem` (time/icon/temp/pop % column).
- `theme/FeverTheme.kt` — Custom `MaterialTheme` with Travello colors + typography (light-mode only).
- `theme/FeverColor.kt` — Travello palette (sky blue, glass white, orange/blue/teal) + `LocalFeverColors` CompositionLocal.
- `theme/FeverType.kt` — Custom typography (56sp Light temperature, adjusted label spacing).

## Resources

- `src/main/res/values/strings.xml` — Default (English) string resources
- `src/main/res/values-de/strings.xml` — German translations
- `src/main/res/values-es/strings.xml` — Spanish translations
- `src/main/res/values-fr/strings.xml` — French translations

## Source

- `src/main/kotlin/com/sls/handbook/feature/fever/` — Feature implementation
- `src/main/kotlin/com/sls/handbook/feature/fever/theme/` — Scoped theme system
- `src/main/res/values/strings.xml` — English string resources
- `src/main/res/values-de/`, `values-es/`, `values-fr/` — German, Spanish, French translations
- `src/test/kotlin/com/sls/handbook/feature/fever/` — Unit tests
  - `WeatherMapperTest.kt` — Tests for current weather, daily forecast, and hourly forecast mapping
  - `HourlyDisplayDataMapperTest.kt` — Tests for hourly display data formatting (time, temperature, pop%)

## Patterns

- **ViewModel + StateFlow:** ViewModel exposes `uiState: StateFlow<FeverUiState>` collected via `collectAsStateWithLifecycle()` in Route
- **Always-rendered WeatherContent:** `WeatherContent` is rendered for all UI states; `FeverUiState` sealed class exposes `weatherDisplay` with `empty()` defaults for `Loading`/`Error`, so transitions are value changes (empty → real data) rather than container visibility toggles
- **Fade animations for data transitions:** `AnimatedVisibility` with `fadeIn`/`fadeOut(tween(FadeDurationMs))` gates sections on data availability; `AnimatedContent` with matching fade spec animates individual text value changes; `Crossfade` animates weather icon swaps. All durations use shared `FadeDurationMs` constant.
- **Route wrapper pattern:** `FeverRoute` wraps `FeverScreen` in `FeverTheme`, ensuring custom theme only applies to this feature
- **CompositionLocal for theme access:** `LocalFeverColors.current` allows nested composables to access extended colors without prop drilling
- **StringResolver for i18n in non-Context classes:** `StringResolver` interface injected via Hilt into ViewModel/Mapper to resolve `R.string.*` resources without direct `Context` dependency
- **Glassmorphism UI:** Semi-transparent white surfaces with borders and soft shadows simulate glass effect
- **Colored stat pills:** Row of cards with colored circular icon backgrounds showing weather metrics (temperature, wind, humidity)
- **Error handling via Snackbar:** Error state triggers a `Snackbar` with retry action using `LaunchedEffect` + `rememberUpdatedState`; keeps the always-rendered WeatherContent pattern intact
- **Dual SwipeHintFab pattern:** Two FABs with directional arrow icons (ArrowBack on left, ArrowForward on right) hinting at swipe gestures, both triggering refresh
- **Horizontally scrollable forecast cards:** Daily forecast and hourly forecast cards use `LazyRow` / `LazyColumn` for horizontal/scrollable layouts within glassmorphic containers
- **Hourly forecast card:** Displays current day's hourly data only (filtered by timezone-aware date matching in repo layer), shows time/icon/temp/precipitation probability, formatted for compact display
- **Edge-to-edge inset handling:**
  - `WeatherContent` column uses `statusBarsPadding()` to avoid overlapping status bar on initial load
  - FABs positioned at bottom with `navigationBarsPadding()` + 24.dp explicit padding to sit above system navigation
  - Imports: `androidx.compose.foundation.layout.{statusBarsPadding, navigationBarsPadding}`

## Notes

- The `FeverTheme` is **intentionally scoped** to the Fever screen via `FeverRoute` wrapper. It does NOT affect other screens (Home, Category, Gallery, TTL Cache), which continue using `HandyPlayTheme` from `:core:designsystem`.
- The theme is light-mode only (sky blue gradient is inherently a light design); does not provide dark mode variants.
- Stat pill icons use `material-icons-extended` (Thermostat, Air, WaterDrop) to avoid bloating core icon sets.
- Do NOT add business logic or state management to composables. Keep all logic in `FeverViewModel`.
- Edge-to-edge display is enabled app-wide via `WindowCompat.setDecorFitsSystemWindows(window, false)` in HandyPlayApp; inset padding is applied per-composable as needed.
- All user-visible strings must use `stringResource()` in composables or `StringResolver` in ViewModel/Mapper for i18n support. Do NOT hardcode strings.
