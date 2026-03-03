# :feature:dp-behavioral

Behavioral design pattern demos — Observer, Strategy, Command, and State Machine.

## Module Info

- **Namespace:** `com.sls.handbook.feature.dp.behavioral`
- **Type:** Feature module
- **Plugin:** `handyplay.android.feature.impl`

## Auto-included by `handyplay.android.feature.impl`

- Compose + Hilt + Lifecycle + Navigation
- `:core:ui`, `:core:designsystem`, `:core:domain`, `:core:model`, `:core:navigation`

## Key Files

### Observer (`observer/`)

- `ObserverRoute.kt` — Navigation wrapper
- `ObserverScreen.kt` — Main composable
- `ObserverViewModel.kt` — `@HiltViewModel` with `StateFlow<ObserverUiState>`
- `ObserverUiState.kt` — Sealed UI state

### Strategy (`strategy/`)

- `StrategyRoute.kt` — Navigation wrapper
- `StrategyScreen.kt` — Main composable
- `StrategyViewModel.kt` — `@HiltViewModel` with `StateFlow<StrategyUiState>`
- `StrategyUiState.kt` — Sealed UI state

### Command (`command/`)

- `CommandRoute.kt` — Navigation wrapper
- `CommandScreen.kt` — Main composable
- `CommandViewModel.kt` — `@HiltViewModel` with `StateFlow<CommandUiState>`
- `CommandUiState.kt` — Sealed UI state

### State Machine (`statemachine/`)

- `StateMachineRoute.kt` — Navigation wrapper
- `StateMachineScreen.kt` — Main composable
- `StateMachineViewModel.kt` — `@HiltViewModel` with `StateFlow<StateMachineUiState>`
- `StateMachineUiState.kt` — Sealed UI state

## Source

- `src/main/kotlin/com/sls/handbook/feature/dp/behavioral/`

## Tests

- `src/test/` — JVM unit tests
  - `ObserverViewModelTest.kt`
  - `StrategyViewModelTest.kt`
  - `CommandViewModelTest.kt`
  - `StateMachineViewModelTest.kt`

## Patterns

- Each pattern follows Route/Screen/ViewModel/UiState structure
- ViewModel exposes `uiState: StateFlow` collected via `collectAsStateWithLifecycle()`
- Navigation destinations: `ObserverDestination`, `StrategyDestination`, `CommandDestination`, `StateMachineDestination`
