package com.sls.handbook.feature.dp.behavioral.statemachine

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class StateMachineViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: StateMachineViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = StateMachineViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is idle with 5 items`() = runTest {
        viewModel.uiState.test {
            val state = awaitItem() as StateMachineUiState.Idle
            assertEquals(VendingState.IDLE, state.currentState)
            assertEquals(5, state.itemCount)
            assertEquals(0, state.coins)
        }
    }

    @Test
    fun `inserting coin transitions from idle to has coin`() = runTest {
        viewModel.uiState.test {
            awaitItem()
            viewModel.onInsertCoin()
            val state = awaitItem() as StateMachineUiState.Idle
            assertEquals(VendingState.HAS_COIN, state.currentState)
            assertEquals(1, state.coins)
        }
    }

    @Test
    fun `selecting item transitions from has coin to dispensing`() = runTest {
        viewModel.uiState.test {
            awaitItem()
            viewModel.onInsertCoin()
            awaitItem()
            viewModel.onSelectItem()
            val state = awaitItem() as StateMachineUiState.Idle
            assertEquals(VendingState.DISPENSING, state.currentState)
        }
    }

    @Test
    fun `dispensing reduces item count and returns to idle`() = runTest {
        viewModel.uiState.test {
            awaitItem()
            viewModel.onInsertCoin()
            awaitItem()
            viewModel.onSelectItem()
            awaitItem()
            viewModel.onDispense()
            val state = awaitItem() as StateMachineUiState.Idle
            assertEquals(VendingState.IDLE, state.currentState)
            assertEquals(4, state.itemCount)
        }
    }

    @Test
    fun `selecting item without coin stays in idle`() = runTest {
        viewModel.uiState.test {
            awaitItem()
            viewModel.onSelectItem()
            val state = awaitItem() as StateMachineUiState.Idle
            assertEquals(VendingState.IDLE, state.currentState)
        }
    }

    @Test
    fun `reset returns to initial state`() = runTest {
        viewModel.uiState.test {
            awaitItem()
            viewModel.onInsertCoin()
            awaitItem()
            viewModel.onReset()
            val state = awaitItem() as StateMachineUiState.Idle
            assertEquals(VendingState.IDLE, state.currentState)
            assertEquals(5, state.itemCount)
            assertEquals(0, state.coins)
        }
    }
}
