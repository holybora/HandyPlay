# :feature:fever

Weather screen displaying random location conditions with Travello-inspired design (gradient background, glassmorphism cards, colored stat pills). Features separate FeverTheme to avoid affecting other screens.

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

- `FeverViewModel.kt` — `@HiltViewModel` with `StateFlow<FeverUiState>`, fetches weather via `WeatherRepository`
- `FeverUiState.kt` — Sealed interface: `Loading`, `Success(weather)`, `Error(message)`
- `FeverRoute.kt` — Route composable wrapping `FeverScreen` in `FeverTheme` for isolated theming
- `FeverScreen.kt` — Main Travello-inspired composable with sky-blue gradient background, hero section (weather icon + stat pills), glassmorphism cards, weather details grid, FAB refresh
- `FeverComponents.kt` — Reusable internal composables: `GlassCard`, `WeatherIconCard`, `GlassDetailCard`
- `theme/FeverTheme.kt` — Custom `MaterialTheme` with Travello color scheme and typography; overrides app-wide theme only for Fever
- `theme/FeverColor.kt` — Travello-inspired colors (sky blue gradient, glass white surfaces, orange/blue/teal accents) and `LocalFeverColors` CompositionLocal for extended color access
- `theme/FeverType.kt` — Custom typography with 56sp Light display temperature, adjusted label spacing

## Source

- `src/main/kotlin/com/sls/handbook/feature/fever/` — Feature implementation
- `src/main/kotlin/com/sls/handbook/feature/fever/theme/` — Scoped theme system

## Patterns

- **ViewModel + StateFlow:** ViewModel exposes `uiState: StateFlow<FeverUiState>` collected via `collectAsStateWithLifecycle()` in Route
- **Route wrapper pattern:** `FeverRoute` wraps `FeverScreen` in `FeverTheme`, ensuring custom theme only applies to this feature
- **CompositionLocal for theme access:** `LocalFeverColors.current` allows nested composables to access extended colors without prop drilling
- **Glassmorphism UI:** Semi-transparent white surfaces with borders and soft shadows simulate glass effect
- **Colored stat pills:** Row of cards with colored circular icon backgrounds showing weather metrics (temperature, wind, humidity)

## Notes

- The `FeverTheme` is **intentionally scoped** to the Fever screen via `FeverRoute` wrapper. It does NOT affect other screens (Home, Category, Gallery, TTL Cache), which continue using `HandyPlayTheme` from `:core:designsystem`.
- The theme is light-mode only (sky blue gradient is inherently a light design); does not provide dark mode variants.
- Stat pill icons use `material-icons-extended` (Thermostat, Air, WaterDrop) to avoid bloating core icon sets.
- Do NOT add business logic or state management to composables. Keep all logic in `FeverViewModel`.
