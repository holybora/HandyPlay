package com.sls.handbook.feature.listvssequence

data class BenchmarkResult(
    val heapAllocatedBytes: Long = 0L,
    val timeSpentMs: Double = 0.0,
    val progress: Float = 0f,
)

sealed interface ListVsSequenceUiState {

    data class Idle(
        val listResult: BenchmarkResult = BenchmarkResult(),
        val sequenceResult: BenchmarkResult = BenchmarkResult(),
        val isRunning: Boolean = false,
    ) : ListVsSequenceUiState
}
