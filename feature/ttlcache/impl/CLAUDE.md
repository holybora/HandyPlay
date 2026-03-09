# :feature:ttlcache

TTL Cache demo screen that fetches random jokes to demonstrate dynamic TTL caching.

## Module Info

- **Namespace:** `com.sls.handbook.feature.ttlcache`
- **Type:** Feature module
- **Plugins:** `handyplay.android.feature.impl`, `handyplay.android.roborazzi`

## Auto-included by `handyplay.android.feature.impl`

- Compose + Hilt + Lifecycle + Navigation
- `:core:ui`, `:core:designsystem`, `:core:domain`, `:core:model`, `:core:navigation`

## Key Files

- `TtlCacheScreen.kt` — Main composable with scrollable column, TTL dropdown, labeled text, code display, and GET button
- `TtlCacheRoute.kt` — Navigation wrapper with `hiltViewModel()` and `collectAsStateWithLifecycle()`
- `TtlCacheViewModel.kt` — `@HiltViewModel` with `StateFlow<TtlCacheUiState>`, fetches jokes via `JokeRepository` with `Exception` error handling
- `TtlCacheUiState.kt` — Sealed interface: `Idle` (ttlSeconds, lastFetchedTime, data, isLoading), `Error`

## Source

- `src/main/kotlin/com/sls/handbook/feature/ttlcache/`

## Tests

- `src/test/` — JVM unit tests
  - `TtlCacheViewModelTest.kt` — ViewModel using MockK + Turbine
  - `TtlCacheUiStateTest.kt` — Sealed interface (Idle, Error) variants tests
  - `TtlCacheScreenScreenshotTest.kt` — Roborazzi screenshot tests (idle, loading, withData states)

## Patterns

- ViewModel exposes `uiState: StateFlow<TtlCacheUiState>` collected via `collectAsStateWithLifecycle()`
- Fetches jokes from `JokeRepository` with configurable TTL (1-5 seconds dropdown)
- Displays "(fresh)" or "(from cache)" labels to demonstrate cache behavior
