package com.sls.handbook.feature.dp.structural.facade

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FacadeViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: FacadeViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = FacadeViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state has facade enabled and all off`() = runTest {
        viewModel.uiState.test {
            val state = awaitItem() as FacadeUiState.Idle
            assertTrue(state.useFacade)
            assertFalse(state.projectorOn)
            assertFalse(state.soundSurround)
            assertFalse(state.lightsDimmed)
        }
    }

    @Test
    fun `watchMovie turns on all subsystems`() = runTest {
        viewModel.uiState.test {
            awaitItem()
            viewModel.onWatchMovie()
            val state = awaitItem() as FacadeUiState.Idle
            assertTrue(state.projectorOn)
            assertTrue(state.soundSurround)
            assertTrue(state.lightsDimmed)
            assertTrue(state.log.any { it.contains("watchMovie") })
        }
    }

    @Test
    fun `stopMovie turns off all subsystems`() = runTest {
        viewModel.uiState.test {
            awaitItem()
            viewModel.onWatchMovie()
            awaitItem()
            viewModel.onStopMovie()
            val state = awaitItem() as FacadeUiState.Idle
            assertFalse(state.projectorOn)
            assertFalse(state.soundSurround)
            assertFalse(state.lightsDimmed)
        }
    }

    @Test
    fun `manual mode toggles individual subsystems`() = runTest {
        viewModel.uiState.test {
            awaitItem()
            viewModel.onToggleFacade(false)
            awaitItem()
            viewModel.onToggleProjector(true)
            val state = awaitItem() as FacadeUiState.Idle
            assertTrue(state.projectorOn)
            assertFalse(state.soundSurround)
            assertFalse(state.lightsDimmed)
        }
    }
}
