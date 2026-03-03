package com.sls.handbook.feature.dp.behavioral.statemachine

import androidx.lifecycle.ViewModel
import com.sls.handbook.core.model.PatternContent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class StateMachineViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<StateMachineUiState>(StateMachineUiState.Idle())
    val uiState: StateFlow<StateMachineUiState> = _uiState.asStateFlow()

    val content = PatternContent(
        title = "State Machine",
        subtitle = "Behavioral Pattern",
        description = "State allows an object to alter its behavior when its internal state changes. " +
            "The object will appear to change its class. Instead of using conditionals to " +
            "determine behavior, each state is encapsulated in its own class, and transitions " +
            "between states are explicit and well-defined.",
        whenToUse = listOf(
            "When an object's behavior depends on its state and it must change at runtime",
            "When operations have large conditional statements that depend on the object's state",
            "When you want to make state transitions explicit and self-documenting",
            "When building UI flows, game logic, or protocol handlers",
        ),
        androidExamples = "Lifecycle states (CREATED, STARTED, RESUMED) form a state machine. " +
            "Navigation component manages back stack as state transitions. " +
            "DownloadManager tracks download states (PENDING, RUNNING, PAUSED, SUCCESSFUL, FAILED). " +
            "Bluetooth connection states (DISCONNECTED, CONNECTING, CONNECTED) are a state machine.",
        structure = """interface State {
    fun handle(context: Machine)
}

class IdleState : State {
    override fun handle(ctx: Machine) {
        // Transition to next state
        ctx.setState(ActiveState())
    }
}

class Machine {
    private var state: State = IdleState()

    fun setState(s: State) {
        state = s
    }

    fun request() {
        state.handle(this)
    }
}""",
    )

    fun onInsertCoin() {
        val currentState = _uiState.value
        if (currentState !is StateMachineUiState.Idle) return

        when (currentState.currentState) {
            VendingState.IDLE -> {
                _uiState.value = currentState.copy(
                    currentState = VendingState.HAS_COIN,
                    coins = currentState.coins + 1,
                    log = currentState.log + "Coin inserted. You can now select an item.",
                )
            }
            VendingState.HAS_COIN -> {
                _uiState.value = currentState.copy(
                    coins = currentState.coins + 1,
                    log = currentState.log + "Extra coin inserted. Total: ${currentState.coins + 1}",
                )
            }
            VendingState.DISPENSING -> {
                _uiState.value = currentState.copy(
                    log = currentState.log + "Please wait, dispensing in progress...",
                )
            }
            VendingState.SOLD_OUT -> {
                _uiState.value = currentState.copy(
                    log = currentState.log + "Machine is sold out. Coin returned.",
                )
            }
        }
    }

    fun onSelectItem() {
        val currentState = _uiState.value
        if (currentState !is StateMachineUiState.Idle) return

        when (currentState.currentState) {
            VendingState.IDLE -> {
                _uiState.value = currentState.copy(
                    log = currentState.log + "Please insert a coin first.",
                )
            }
            VendingState.HAS_COIN -> {
                _uiState.value = currentState.copy(
                    currentState = VendingState.DISPENSING,
                    log = currentState.log + "Item selected. Dispensing...",
                )
            }
            VendingState.DISPENSING -> {
                _uiState.value = currentState.copy(
                    log = currentState.log + "Already dispensing, please wait.",
                )
            }
            VendingState.SOLD_OUT -> {
                _uiState.value = currentState.copy(
                    log = currentState.log + "Machine is sold out.",
                )
            }
        }
    }

    fun onDispense() {
        val currentState = _uiState.value
        if (currentState !is StateMachineUiState.Idle) return

        when (currentState.currentState) {
            VendingState.DISPENSING -> {
                val newItemCount = currentState.itemCount - 1
                val nextState = if (newItemCount <= 0) VendingState.SOLD_OUT else VendingState.IDLE
                val message = if (newItemCount <= 0) {
                    "Item dispensed. Machine is now sold out!"
                } else {
                    "Item dispensed! $newItemCount items remaining."
                }
                _uiState.value = currentState.copy(
                    currentState = nextState,
                    coins = currentState.coins - 1,
                    itemCount = newItemCount,
                    log = currentState.log + message,
                )
            }
            else -> {
                _uiState.value = currentState.copy(
                    log = currentState.log + "Nothing to dispense.",
                )
            }
        }
    }

    fun onReset() {
        _uiState.value = StateMachineUiState.Idle()
    }
}
