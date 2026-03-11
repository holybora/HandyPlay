# :feature:category

Category topics screen with topic grid and search functionality.

## Module Info

- **Namespace:** `com.sls.handbook.feature.category`
- **Type:** Feature module
- **Plugins:** `handyplay.android.feature.impl`, `handyplay.android.roborazzi`

## Auto-included by `handyplay.android.feature.impl`

- Compose + Hilt + Lifecycle + Navigation
- `:core:ui`, `:core:designsystem`, `:core:domain`, `:core:model`, `:core:navigation`

## Key Files

- `CategoryScreen.kt` — Main composable with scrollable `LazyVerticalGrid` (2-column layout), no UI chrome (search moved to app level)
- `CategoryRoute.kt` — Navigation wrapper receiving `searchQuery` from app level, syncs to ViewModel via `LaunchedEffect`
- `CategoryViewModel.kt` — `@HiltViewModel` with `StateFlow<CategoryUiState>`, search filtering, uses `GetTopicsByCategoryIdUseCase`
- `CategoryUiState.kt` — Sealed interface: `Loading`, `Success`, `Error`
- `components/TopicCard.kt` — Card composable for topic display (AccentAmber)

## Source

- `src/main/kotlin/com/sls/handbook/feature/category/`

## Tests

- `src/test/` — JVM unit tests
  - `CategoryUiStateTest.kt` — Sealed interface variants (Loading, Success, Error) tests
  - `CategoryScreenScreenshotTest.kt` — Roborazzi screenshot tests (light, dark, loading, empty states)

## Patterns

- ViewModel exposes `uiState: StateFlow<CategoryUiState>` collected via `collectAsStateWithLifecycle()`
- Navigation args extracted via `savedStateHandle.toRoute<CategoryDestination>()`
- Search filtering triggered by `onSearchQueryChanged(query: String)` from app-level BottomSearchBar
- Route receives search query via parameter and syncs to ViewModel via `LaunchedEffect` for filtering
