package com.sls.handbook.feature.dp.structural.decorator

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
class DecoratorViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: DecoratorViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = DecoratorViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is basic coffee at 2 dollars`() = runTest {
        viewModel.uiState.test {
            val state = awaitItem() as DecoratorUiState.Idle
            assertEquals("Basic Coffee", state.description)
            assertEquals(2.00, state.price, 0.01)
        }
    }

    @Test
    fun `adding milk increases price by 50 cents`() = runTest {
        viewModel.uiState.test {
            awaitItem()
            viewModel.onToggleMilk(true)
            val state = awaitItem() as DecoratorUiState.Idle
            assertEquals(2.50, state.price, 0.01)
            assertTrue(state.description.contains("Milk"))
        }
    }

    @Test
    fun `adding all decorators accumulates price`() = runTest {
        viewModel.uiState.test {
            awaitItem()
            viewModel.onToggleMilk(true)
            awaitItem()
            viewModel.onToggleSugar(true)
            awaitItem()
            viewModel.onToggleWhippedCream(true)
            awaitItem()
            viewModel.onToggleCaramel(true)
            val state = awaitItem() as DecoratorUiState.Idle
            assertEquals(4.10, state.price, 0.01)
        }
    }

    @Test
    fun `removing decorator reduces price`() = runTest {
        viewModel.uiState.test {
            awaitItem()
            viewModel.onToggleMilk(true)
            awaitItem()
            viewModel.onToggleMilk(false)
            val state = awaitItem() as DecoratorUiState.Idle
            assertEquals(2.00, state.price, 0.01)
        }
    }
}
