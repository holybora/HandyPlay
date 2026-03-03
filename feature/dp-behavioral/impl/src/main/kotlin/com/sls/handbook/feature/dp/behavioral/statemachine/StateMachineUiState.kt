package com.sls.handbook.feature.dp.behavioral.statemachine

sealed interface StateMachineUiState {
    data class Idle(
        val currentState: VendingState = VendingState.IDLE,
        val coins: Int = 0,
        val itemCount: Int = 5,
        val log: List<String> = listOf("Machine ready. Insert a coin to start."),
    ) : StateMachineUiState
}

enum class VendingState(val displayName: String) {
    IDLE("Idle"),
    HAS_COIN("Has Coin"),
    DISPENSING("Dispensing"),
    SOLD_OUT("Sold Out"),
}
