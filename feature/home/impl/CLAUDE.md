# :feature:home

Home screen with category grid and search functionality.

## Module Info

- **Namespace:** `com.sls.handbook.feature.home`
- **Type:** Feature module
- **Plugin:** `handyplay.android.feature`

## Auto-included by `handyplay.android.feature`

- Compose + Hilt + Lifecycle + Navigation
- `:core:ui`, `:core:designsystem`, `:core:domain`, `:core:model`, `:navigation`

## Key Files

- `HomeScreen.kt` — Main composable with scrollable `LazyVerticalGrid` (2-column layout), no UI chrome (search moved to app level)
- `HomeRoute.kt` — Navigation wrapper receiving `searchQuery` from app level, syncs to ViewModel via `LaunchedEffect`
- `HomeViewModel.kt` — `@HiltViewModel` with `StateFlow<HomeUiState>`, search filtering
- `HomeUiState.kt` — Sealed interface: `Loading`, `Success`, `Error`
- `components/CategoryCard.kt` — Card composable for category display

## Source

- `src/main/kotlin/com/sls/handbook/feature/home/`

## Patterns

- ViewModel exposes `uiState: StateFlow<HomeUiState>` collected via `collectAsStateWithLifecycle()`
- Search filtering triggered by `onSearchQueryChanged(query: String)` from app-level BottomSearchBar
- Route receives search query via parameter and syncs to ViewModel via `LaunchedEffect` for filtering
