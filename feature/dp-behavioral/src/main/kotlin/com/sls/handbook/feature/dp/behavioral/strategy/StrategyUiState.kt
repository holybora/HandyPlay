package com.sls.handbook.feature.dp.behavioral.strategy

sealed interface StrategyUiState {
    data class Idle(
        val numbers: List<Int> = DEFAULT_NUMBERS,
        val selectedStrategy: SortStrategyType = SortStrategyType.BUBBLE,
        val sortedNumbers: List<Int>? = null,
        val stepCount: Int = 0,
        val comparisonCount: Int = 0,
    ) : StrategyUiState {
        companion object {
            val DEFAULT_NUMBERS = listOf(38, 27, 43, 3, 9, 82, 10)
        }
    }
}

enum class SortStrategyType(val displayName: String) {
    BUBBLE("Bubble Sort"),
    SELECTION("Selection Sort"),
    INSERTION("Insertion Sort"),
}
