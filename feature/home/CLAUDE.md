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

- `HomeScreen.kt` — Main composable with `LazyVerticalGrid` (2-column layout)
- `HomeRoute.kt` — Navigation wrapper
- `HomeViewModel.kt` — `@HiltViewModel` with `StateFlow<HomeUiState>`, search filtering
- `HomeUiState.kt` — Sealed interface: `Loading`, `Success`, `Error`
- `components/CategoryCard.kt` — Card composable for category display

## Source

- `src/main/java/com/sls/handbook/feature/home/`

## Patterns

- ViewModel exposes `uiState: StateFlow<HomeUiState>` collected via `collectAsStateWithLifecycle()`
- Search filters the local category list via `onSearchQueryChanged(query: String)`
