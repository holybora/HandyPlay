package com.sls.handbook.feature.dp.behavioral.strategy

import androidx.lifecycle.ViewModel
import com.sls.handbook.core.model.PatternContent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class StrategyViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<StrategyUiState>(StrategyUiState.Idle())
    val uiState: StateFlow<StrategyUiState> = _uiState.asStateFlow()

    val content = PatternContent(
        title = "Strategy",
        subtitle = "Behavioral Pattern",
        description = "Strategy defines a family of algorithms, encapsulates each one, and makes " +
            "them interchangeable. Strategy lets the algorithm vary independently from " +
            "clients that use it. The context delegates work to a strategy object instead " +
            "of implementing the algorithm directly.",
        whenToUse = listOf(
            "When you want to define a class that will have one behavior that is similar to other behaviors in a list",
            "When you need to use one of several variations of an algorithm",
            "When an algorithm uses data that clients shouldn't know about",
            "When you want to swap algorithms at runtime without modifying the context",
        ),
        androidExamples = "DiffUtil.ItemCallback is a strategy for calculating list diffs. " +
            "Comparator implementations are strategies for sorting. " +
            "Retrofit CallAdapter.Factory uses strategy pattern for response handling. " +
            "Animation interpolators (LinearInterpolator, AccelerateInterpolator) are strategies.",
        structure = """interface Strategy {
    fun execute(data: List<Int>): Result
}

class ConcreteStrategyA : Strategy {
    override fun execute(data: List<Int>) =
        // Algorithm A
}

class Context(
    private var strategy: Strategy
) {
    fun setStrategy(s: Strategy) {
        strategy = s
    }
    fun doWork(data: List<Int>) =
        strategy.execute(data)
}""",
    )

    fun onStrategySelected(strategy: SortStrategyType) {
        _uiState.update { currentState ->
            if (currentState is StrategyUiState.Idle) {
                currentState.copy(
                    selectedStrategy = strategy,
                    sortedNumbers = null,
                    stepCount = 0,
                    comparisonCount = 0,
                )
            } else {
                currentState
            }
        }
    }

    fun onSort() {
        val currentState = _uiState.value
        if (currentState !is StrategyUiState.Idle) return

        val strategy: SortStrategy = when (currentState.selectedStrategy) {
            SortStrategyType.BUBBLE -> BubbleSortStrategy()
            SortStrategyType.SELECTION -> SelectionSortStrategy()
            SortStrategyType.INSERTION -> InsertionSortStrategy()
        }

        val result = strategy.sort(currentState.numbers.toMutableList())
        _uiState.value = currentState.copy(
            sortedNumbers = result.sorted,
            stepCount = result.steps,
            comparisonCount = result.comparisons,
        )
    }

    fun onShuffle() {
        _uiState.update { currentState ->
            if (currentState is StrategyUiState.Idle) {
                currentState.copy(
                    numbers = (1..MAX_SHUFFLE_RANGE).shuffled().take(NUMBERS_COUNT),
                    sortedNumbers = null,
                    stepCount = 0,
                    comparisonCount = 0,
                )
            } else {
                currentState
            }
        }
    }

    companion object {
        private const val MAX_SHUFFLE_RANGE = 50
        private const val NUMBERS_COUNT = 7
    }

    private data class SortResult(
        val sorted: List<Int>,
        val steps: Int,
        val comparisons: Int,
    )

    private interface SortStrategy {
        fun sort(list: MutableList<Int>): SortResult
    }

    private class BubbleSortStrategy : SortStrategy {
        override fun sort(list: MutableList<Int>): SortResult {
            var steps = 0
            var comparisons = 0
            for (i in list.indices) {
                for (j in 0 until list.size - 1 - i) {
                    comparisons++
                    if (list[j] > list[j + 1]) {
                        val temp = list[j]
                        list[j] = list[j + 1]
                        list[j + 1] = temp
                        steps++
                    }
                }
            }
            return SortResult(list.toList(), steps, comparisons)
        }
    }

    private class SelectionSortStrategy : SortStrategy {
        override fun sort(list: MutableList<Int>): SortResult {
            var steps = 0
            var comparisons = 0
            for (i in list.indices) {
                var minIdx = i
                for (j in i + 1 until list.size) {
                    comparisons++
                    if (list[j] < list[minIdx]) {
                        minIdx = j
                    }
                }
                if (minIdx != i) {
                    val temp = list[i]
                    list[i] = list[minIdx]
                    list[minIdx] = temp
                    steps++
                }
            }
            return SortResult(list.toList(), steps, comparisons)
        }
    }

    private class InsertionSortStrategy : SortStrategy {
        override fun sort(list: MutableList<Int>): SortResult {
            var steps = 0
            var comparisons = 0
            for (i in 1 until list.size) {
                val key = list[i]
                var j = i - 1
                while (j >= 0) {
                    comparisons++
                    if (list[j] > key) {
                        list[j + 1] = list[j]
                        j--
                        steps++
                    } else {
                        break
                    }
                }
                list[j + 1] = key
            }
            return SortResult(list.toList(), steps, comparisons)
        }
    }
}
