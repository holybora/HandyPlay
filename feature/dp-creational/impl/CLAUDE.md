# :feature:dp-creational

Creational design pattern demos — Factory Method, Abstract Factory, and Prototype.

## Module Info

- **Namespace:** `com.sls.handbook.feature.dp.creational`
- **Type:** Feature module
- **Plugin:** `handyplay.android.feature.impl`

## Auto-included by `handyplay.android.feature.impl`

- Compose + Hilt + Lifecycle + Navigation
- `:core:ui`, `:core:designsystem`, `:core:domain`, `:core:model`, `:core:navigation`

## Key Files

### Factory Method (`factorymethod/`)

- `FactoryMethodRoute.kt` — Navigation wrapper
- `FactoryMethodScreen.kt` — Main composable
- `FactoryMethodViewModel.kt` — `@HiltViewModel` with `StateFlow<FactoryMethodUiState>`
- `FactoryMethodUiState.kt` — Sealed UI state

### Abstract Factory (`abstractfactory/`)

- `AbstractFactoryRoute.kt` — Navigation wrapper
- `AbstractFactoryScreen.kt` — Main composable
- `AbstractFactoryViewModel.kt` — `@HiltViewModel` with `StateFlow<AbstractFactoryUiState>`
- `AbstractFactoryUiState.kt` — Sealed UI state

### Prototype (`prototype/`)

- `PrototypeRoute.kt` — Navigation wrapper
- `PrototypeScreen.kt` — Main composable
- `PrototypeViewModel.kt` — `@HiltViewModel` with `StateFlow<PrototypeUiState>`
- `PrototypeUiState.kt` — Sealed UI state

## Source

- `src/main/kotlin/com/sls/handbook/feature/dp/creational/`

## Tests

- `src/test/` — JVM unit tests
  - `FactoryMethodViewModelTest.kt`
  - `AbstractFactoryViewModelTest.kt`
  - `PrototypeViewModelTest.kt`

## Patterns

- Each pattern follows Route/Screen/ViewModel/UiState structure
- ViewModel exposes `uiState: StateFlow` collected via `collectAsStateWithLifecycle()`
- Navigation destinations: `FactoryMethodDestination`, `AbstractFactoryDestination`, `PrototypeDestination`
