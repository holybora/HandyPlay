package com.sls.handbook.feature.dp.structural.decorator

sealed interface DecoratorUiState {
    data class Idle(
        val hasMilk: Boolean = false,
        val hasSugar: Boolean = false,
        val hasWhippedCream: Boolean = false,
        val hasCaramel: Boolean = false,
        val description: String = "Basic Coffee",
        val price: Double = 2.00,
    ) : DecoratorUiState
}
