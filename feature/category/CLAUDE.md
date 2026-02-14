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

- `CategoryScreen.kt` — Main composable with `LazyVerticalGrid` (2-column layout)
- `CategoryRoute.kt` — Navigation wrapper
- `CategoryViewModel.kt` — `@HiltViewModel` with `StateFlow<CategoryUiState>`, search filtering, mock topics
- `CategoryUiState.kt` — Sealed interface: `Loading`, `Success`, `Error`
- `components/TopicCard.kt` — Card composable for topic display (AccentAmber)

## Source

- `src/main/kotlin/com/sls/handbook/feature/category/`

## Patterns

- ViewModel exposes `uiState: StateFlow<CategoryUiState>` collected via `collectAsStateWithLifecycle()`
- Navigation args extracted via `savedStateHandle.toRoute<CategoryDestination>()`
- Search filters the local topic list via `onSearchQueryChanged(query: String)`
- Uses shared `BreadcrumbBar` and `SearchBar` from `:core:ui`
