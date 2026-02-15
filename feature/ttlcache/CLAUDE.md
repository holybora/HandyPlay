# :feature:ttlcache

TTL Cache demo screen that fetches random jokes to demonstrate dynamic TTL caching.

## Module Info

- **Namespace:** `com.sls.handbook.feature.ttlcache`
- **Type:** Feature module
- **Plugin:** `handyplay.android.feature`

## Auto-included by `handyplay.android.feature`

- Compose + Hilt + Lifecycle + Navigation
- `:core:ui`, `:core:designsystem`, `:core:domain`, `:core:model`, `:navigation`

## Dependencies

- `:core:data` (implementation) — repository access
- `:core:common` (implementation) — cache utilities

## Key Files

- `TtlCacheScreen.kt` — Main composable with scrollable column, TTL dropdown, labeled text, code display, and GET button
- `TtlCacheRoute.kt` — Navigation wrapper with `hiltViewModel()` and `collectAsStateWithLifecycle()`
- `TtlCacheViewModel.kt` — `@HiltViewModel` with `StateFlow<TtlCacheUiState>`, fetches jokes via `JokeRepository` with `Exception` error handling
- `TtlCacheUiState.kt` — Sealed interface: `Idle` (ttlSeconds, lastFetchedTime, data, isLoading), `Error`

## Source

- `src/main/kotlin/com/sls/handbook/feature/ttlcache/`

## Tests

- `src/test/` — JVM unit tests for `TtlCacheViewModel` using MockK + Turbine

## Patterns

- ViewModel exposes `uiState: StateFlow<TtlCacheUiState>` collected via `collectAsStateWithLifecycle()`
- Fetches jokes from `JokeRepository` with configurable TTL (1-5 seconds dropdown)
- Displays "(fresh)" or "(from cache)" labels to demonstrate cache behavior
