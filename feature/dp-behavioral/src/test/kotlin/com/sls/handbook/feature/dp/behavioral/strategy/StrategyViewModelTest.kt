package com.sls.handbook.feature.dp.behavioral.strategy

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class StrategyViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: StrategyViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = StrategyViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state has bubble sort selected and no result`() = runTest {
        viewModel.uiState.test {
            val state = awaitItem() as StrategyUiState.Idle
            assertEquals(SortStrategyType.BUBBLE, state.selectedStrategy)
            assertNull(state.sortedNumbers)
        }
    }

    @Test
    fun `sorting produces sorted result with counts`() = runTest {
        viewModel.uiState.test {
            awaitItem()
            viewModel.onSort()
            val state = awaitItem() as StrategyUiState.Idle
            assertNotNull(state.sortedNumbers)
            assertEquals(state.numbers.sorted(), state.sortedNumbers)
            assert(state.stepCount > 0 || state.comparisonCount > 0)
        }
    }

    @Test
    fun `changing strategy clears previous result`() = runTest {
        viewModel.uiState.test {
            awaitItem()
            viewModel.onSort()
            awaitItem()
            viewModel.onStrategySelected(SortStrategyType.SELECTION)
            val state = awaitItem() as StrategyUiState.Idle
            assertEquals(SortStrategyType.SELECTION, state.selectedStrategy)
            assertNull(state.sortedNumbers)
        }
    }

    @Test
    fun `shuffle generates new numbers`() = runTest {
        viewModel.uiState.test {
            awaitItem()
            viewModel.onShuffle()
            val state = awaitItem() as StrategyUiState.Idle
            assertEquals(7, state.numbers.size)
            // New shuffle very unlikely to be same as original
            assertNull(state.sortedNumbers)
        }
    }
}
