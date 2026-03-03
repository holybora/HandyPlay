package com.sls.handbook.feature.dp.behavioral.observer

sealed interface ObserverUiState {
    data class Idle(
        val stockPrice: Double = 100.0,
        val portfolioSubscribed: Boolean = true,
        val alertSubscribed: Boolean = true,
        val chartSubscribed: Boolean = true,
        val portfolioValue: String = "$100.00",
        val alertMessage: String = "Price is stable",
        val chartTrend: String = "→ Flat",
    ) : ObserverUiState
}
