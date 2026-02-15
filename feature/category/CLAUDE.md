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

- `CategoryScreen.kt` — Main composable with scrollable `LazyVerticalGrid` (2-column layout), fixed bottom search/breadcrumbs
- `CategoryRoute.kt` — Navigation wrapper with `onTopicClick` and `onBreadcrumbClick` callbacks
- `CategoryViewModel.kt` — `@HiltViewModel` with `StateFlow<CategoryUiState>`, search filtering, mock topics using `Topic.ID_TTL_CACHE` constants
- `CategoryUiState.kt` — Sealed interface: `Loading`, `Success`, `Error`
- `components/TopicCard.kt` — Card composable for topic display (AccentAmber)

## Source

- `src/main/kotlin/com/sls/handbook/feature/category/`

## Patterns

- ViewModel exposes `uiState: StateFlow<CategoryUiState>` collected via `collectAsStateWithLifecycle()`
- Navigation args extracted via `savedStateHandle.toRoute<CategoryDestination>()`
- Search filters the local topic list via `onSearchQueryChanged(query: String)`
- Uses `BottomSearchBar` from `:core:ui` for fixed bottom placement with keyboard awareness
