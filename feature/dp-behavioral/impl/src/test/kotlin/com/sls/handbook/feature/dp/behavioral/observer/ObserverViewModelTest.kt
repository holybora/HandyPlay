package com.sls.handbook.feature.dp.behavioral.observer

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ObserverViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: ObserverViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ObserverViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state has all subscribers subscribed`() = runTest {
        viewModel.uiState.test {
            val state = awaitItem() as ObserverUiState.Idle
            assertTrue(state.portfolioSubscribed)
            assertTrue(state.alertSubscribed)
            assertTrue(state.chartSubscribed)
            assertEquals(100.0, state.stockPrice, 0.01)
        }
    }

    @Test
    fun `price change updates subscribed observers`() = runTest {
        viewModel.uiState.test {
            awaitItem()
            viewModel.onPriceChanged(110.0)
            val state = awaitItem() as ObserverUiState.Idle
            assertEquals(110.0, state.stockPrice, 0.01)
            assertTrue(state.portfolioValue.contains("110.00"))
        }
    }

    @Test
    fun `unsubscribed observer does not update on price change`() = runTest {
        viewModel.uiState.test {
            awaitItem()
            viewModel.onTogglePortfolio(false)
            val afterUnsubscribe = awaitItem() as ObserverUiState.Idle
            val frozenValue = afterUnsubscribe.portfolioValue

            viewModel.onPriceChanged(150.0)
            val state = awaitItem() as ObserverUiState.Idle
            assertEquals(frozenValue, state.portfolioValue)
        }
    }

    @Test
    fun `toggling alert subscription updates state`() = runTest {
        viewModel.uiState.test {
            awaitItem()
            viewModel.onToggleAlert(false)
            val state = awaitItem() as ObserverUiState.Idle
            assertEquals(false, state.alertSubscribed)
        }
    }
}
