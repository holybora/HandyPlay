# :feature:category

Category topics screen with topic grid and search functionality.

## Module Info

- **Namespace:** `com.sls.handbook.feature.category`
- **Type:** Feature module
- **Plugin:** `handyplay.android.feature`

## Auto-included by `handyplay.android.feature`

- Compose + Hilt + Lifecycle + Navigation
- `:core:ui`, `:core:designsystem`, `:core:domain`, `:core:model`, `:navigation`

## Key Files

- `CategoryScreen.kt` — Main composable with scrollable `LazyVerticalGrid` (2-column layout), no UI chrome (search moved to app level)
- `CategoryRoute.kt` — Navigation wrapper receiving `searchQuery` from app level, syncs to ViewModel via `LaunchedEffect`
- `CategoryViewModel.kt` — `@HiltViewModel` with `StateFlow<CategoryUiState>`, search filtering, mock topics using `Topic.ID_TTL_CACHE` constants
- `CategoryUiState.kt` — Sealed interface: `Loading`, `Success`, `Error`
- `components/TopicCard.kt` — Card composable for topic display (AccentAmber)

## Source

- `src/main/kotlin/com/sls/handbook/feature/category/`

## Patterns

- ViewModel exposes `uiState: StateFlow<CategoryUiState>` collected via `collectAsStateWithLifecycle()`
- Navigation args extracted via `savedStateHandle.toRoute<CategoryDestination>()`
- Search filtering triggered by `onSearchQueryChanged(query: String)` from app-level BottomSearchBar
- Route receives search query via parameter and syncs to ViewModel via `LaunchedEffect` for filtering
