package com.sls.handbook.feature.listvssequence

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListVsSequenceViewModel @Inject constructor() : ViewModel() {

    internal var backgroundDispatcher: CoroutineDispatcher = Dispatchers.IO
    internal var perElementDelayMs: Long = PER_ELEMENT_DELAY_MS

    private val _uiState = MutableStateFlow<ListVsSequenceUiState>(
        ListVsSequenceUiState.Idle(),
    )
    val uiState: StateFlow<ListVsSequenceUiState> = _uiState.asStateFlow()

    fun onStartClick() {
        val currentState = _uiState.value
        if (currentState !is ListVsSequenceUiState.Idle || currentState.isRunning) return

        _uiState.value = ListVsSequenceUiState.Idle(isRunning = true)

        viewModelScope.launch {
            val listDeferred = async(backgroundDispatcher) {
                runListBenchmark { progress ->
                    _uiState.update { state ->
                        (state as? ListVsSequenceUiState.Idle)?.copy(
                            listResult = state.listResult.copy(progress = progress),
                        ) ?: state
                    }
                }
            }

            val sequenceDeferred = async(backgroundDispatcher) {
                runSequenceBenchmark { progress ->
                    _uiState.update { state ->
                        (state as? ListVsSequenceUiState.Idle)?.copy(
                            sequenceResult = state.sequenceResult.copy(progress = progress),
                        ) ?: state
                    }
                }
            }

            val listResult = listDeferred.await()
            val sequenceResult = sequenceDeferred.await()

            _uiState.value = ListVsSequenceUiState.Idle(
                listResult = listResult,
                sequenceResult = sequenceResult,
                isRunning = false,
            )
        }
    }

    internal fun runListBenchmark(onProgress: (Float) -> Unit): BenchmarkResult {
        val elementDelay = perElementDelayMs
        val totalOps = RANGE_SIZE * 2
        var completedOps = 0
        val startTime = System.nanoTime()

        val step1 = (1..RANGE_SIZE).toList()
        val step2 = step1.map {
            Thread.sleep(elementDelay)
            completedOps++
            onProgress(completedOps.toFloat() / totalOps)
            it * MULTIPLY_FACTOR
        }
        val step3 = step2.filter {
            Thread.sleep(elementDelay)
            completedOps++
            onProgress(completedOps.toFloat() / totalOps)
            it > FILTER_THRESHOLD
        }
        val step4 = step3.take(TAKE_COUNT)

        val endTime = System.nanoTime()
        step4.size

        val totalElements = step1.size.toLong() + step2.size + step3.size + step4.size

        return BenchmarkResult(
            heapAllocatedBytes = totalElements * ESTIMATED_BYTES_PER_ELEMENT,
            timeSpentMs = (endTime - startTime) / NANOS_PER_MS,
            progress = 1f,
        )
    }

    internal fun runSequenceBenchmark(onProgress: (Float) -> Unit): BenchmarkResult {
        val elementDelay = perElementDelayMs
        val startTime = System.nanoTime()

        val result = (1..RANGE_SIZE).asSequence()
            .map {
                Thread.sleep(elementDelay)
                it * MULTIPLY_FACTOR
            }
            .filter {
                Thread.sleep(elementDelay)
                it > FILTER_THRESHOLD
            }
            .take(TAKE_COUNT)
            .toList()

        val endTime = System.nanoTime()
        result.size

        val totalElements = result.size.toLong()
        onProgress(1f)

        return BenchmarkResult(
            heapAllocatedBytes = totalElements * ESTIMATED_BYTES_PER_ELEMENT,
            timeSpentMs = (endTime - startTime) / NANOS_PER_MS,
            progress = 1f,
        )
    }

    companion object {
        private const val RANGE_SIZE = 1_000
        private const val MULTIPLY_FACTOR = 2
        private const val FILTER_THRESHOLD = 100
        private const val TAKE_COUNT = 10
        private const val NANOS_PER_MS = 1_000_000.0
        private const val PER_ELEMENT_DELAY_MS = 1L

        /** 16-byte Integer object + 4-byte array reference */
        private const val ESTIMATED_BYTES_PER_ELEMENT = 20L
    }
}
