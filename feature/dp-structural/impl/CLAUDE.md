# :feature:dp-structural

Structural design pattern demos — Adapter, Decorator, and Facade.

## Module Info

- **Namespace:** `com.sls.handbook.feature.dp.structural`
- **Type:** Feature module
- **Plugin:** `handyplay.android.feature`

## Auto-included by `handyplay.android.feature`

- Compose + Hilt + Lifecycle + Navigation
- `:core:ui`, `:core:designsystem`, `:core:domain`, `:core:model`, `:navigation`

## Key Files

### Adapter (`adapter/`)

- `AdapterPatternRoute.kt` — Navigation wrapper
- `AdapterPatternScreen.kt` — Main composable
- `AdapterPatternViewModel.kt` — `@HiltViewModel` with `StateFlow<AdapterPatternUiState>`
- `AdapterPatternUiState.kt` — Sealed UI state

### Decorator (`decorator/`)

- `DecoratorRoute.kt` — Navigation wrapper
- `DecoratorScreen.kt` — Main composable
- `DecoratorViewModel.kt` — `@HiltViewModel` with `StateFlow<DecoratorUiState>`
- `DecoratorUiState.kt` — Sealed UI state

### Facade (`facade/`)

- `FacadeRoute.kt` — Navigation wrapper
- `FacadeScreen.kt` — Main composable
- `FacadeViewModel.kt` — `@HiltViewModel` with `StateFlow<FacadeUiState>`
- `FacadeUiState.kt` — Sealed UI state

## Source

- `src/main/kotlin/com/sls/handbook/feature/dp/structural/`

## Tests

- `src/test/` — JVM unit tests
  - `AdapterPatternViewModelTest.kt`
  - `DecoratorViewModelTest.kt`
  - `FacadeViewModelTest.kt`

## Patterns

- Each pattern follows Route/Screen/ViewModel/UiState structure
- ViewModel exposes `uiState: StateFlow` collected via `collectAsStateWithLifecycle()`
- Navigation destinations: `AdapterPatternDestination`, `DecoratorDestination`, `FacadeDestination`
