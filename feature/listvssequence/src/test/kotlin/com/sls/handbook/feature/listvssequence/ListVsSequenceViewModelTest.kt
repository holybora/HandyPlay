package com.sls.handbook.feature.listvssequence

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ListVsSequenceViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: ListVsSequenceViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ListVsSequenceViewModel().apply {
            backgroundDispatcher = testDispatcher
            perElementDelayMs = 0L
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Idle with zero results`() = runTest {
        viewModel.uiState.test {
            val state = awaitItem() as ListVsSequenceUiState.Idle
            assertFalse(state.isRunning)
            assertEquals(0L, state.listResult.heapAllocatedBytes)
            assertEquals(0L, state.sequenceResult.heapAllocatedBytes)
            assertEquals(0f, state.listResult.progress)
            assertEquals(0f, state.sequenceResult.progress)
        }
    }

    @Test
    fun `onStartClick sets isRunning to true`() = runTest {
        viewModel.uiState.test {
            awaitItem() // initial idle
            viewModel.onStartClick()
            val running = awaitItem() as ListVsSequenceUiState.Idle
            assertTrue(running.isRunning)
        }
    }

    @Test
    fun `onStartClick completes with results`() = runTest {
        viewModel.uiState.test {
            awaitItem() // initial idle
            viewModel.onStartClick()
            advanceUntilIdle()

            val finalState = expectMostRecentItem() as ListVsSequenceUiState.Idle
            assertFalse(finalState.isRunning)
            assertEquals(1f, finalState.listResult.progress)
            assertEquals(1f, finalState.sequenceResult.progress)
            assertTrue(finalState.listResult.timeSpentMs > 0)
            assertTrue(finalState.sequenceResult.timeSpentMs > 0)
            assertTrue(finalState.listResult.heapAllocatedBytes > 0)
            assertTrue(finalState.sequenceResult.heapAllocatedBytes > 0)
        }
    }

    @Test
    fun `list allocates more memory than sequence`() = runTest {
        viewModel.uiState.test {
            awaitItem() // initial idle
            viewModel.onStartClick()
            advanceUntilIdle()

            val finalState = expectMostRecentItem() as ListVsSequenceUiState.Idle
            assertTrue(
                finalState.listResult.heapAllocatedBytes >
                    finalState.sequenceResult.heapAllocatedBytes,
            )
        }
    }

    @Test
    fun `onStartClick ignored when already running`() = runTest {
        viewModel.uiState.test {
            awaitItem() // initial idle
            viewModel.onStartClick()
            val running = awaitItem() as ListVsSequenceUiState.Idle
            assertTrue(running.isRunning)

            // Second click should be ignored — no new emission
            viewModel.onStartClick()
        }
    }
}
