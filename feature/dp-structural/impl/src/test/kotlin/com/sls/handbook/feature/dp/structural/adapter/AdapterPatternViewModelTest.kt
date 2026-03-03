package com.sls.handbook.feature.dp.structural.adapter

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AdapterPatternViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: AdapterPatternViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = AdapterPatternViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state uses metric adapter`() = runTest {
        viewModel.uiState.test {
            val state = awaitItem() as AdapterPatternUiState.Idle
            assertTrue(state.useMetric)
            assertTrue(state.adaptedData.temperature.contains("°C"))
        }
    }

    @Test
    fun `switching to imperial shows Fahrenheit`() = runTest {
        viewModel.uiState.test {
            awaitItem()
            viewModel.onSystemToggle(false)
            val state = awaitItem() as AdapterPatternUiState.Idle
            assertTrue(state.adaptedData.temperature.contains("°F"))
            assertTrue(state.adaptedData.distance.contains("mi"))
        }
    }

    @Test
    fun `changing temperature re-adapts data`() = runTest {
        viewModel.uiState.test {
            awaitItem()
            viewModel.onTemperatureChanged(100.0)
            val state = awaitItem() as AdapterPatternUiState.Idle
            assertTrue(state.adaptedData.temperature.contains("37"))
        }
    }
}
