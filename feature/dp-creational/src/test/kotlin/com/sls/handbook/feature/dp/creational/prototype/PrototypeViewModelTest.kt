package com.sls.handbook.feature.dp.creational.prototype

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
class PrototypeViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: PrototypeViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = PrototypeViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state has blue circle original and empty clones`() = runTest {
        viewModel.uiState.test {
            val state = awaitItem() as PrototypeUiState.Idle
            assertEquals(ShapeType.CIRCLE, state.originalShape.type)
            assertEquals("Blue", state.originalShape.colorName)
            assertTrue(state.clones.isEmpty())
        }
    }

    @Test
    fun `onClone creates a copy of the original`() = runTest {
        viewModel.uiState.test {
            awaitItem()
            viewModel.onClone()
            val state = awaitItem() as PrototypeUiState.Idle
            assertEquals(1, state.clones.size)
            assertEquals(state.originalShape.type, state.clones[0].type)
            assertEquals(state.originalShape.colorName, state.clones[0].colorName)
        }
    }

    @Test
    fun `modifying clone color does not affect original`() = runTest {
        viewModel.uiState.test {
            awaitItem()
            viewModel.onClone()
            awaitItem()
            viewModel.onCloneColorChanged(0, "Red", 0xFFF44336)
            val state = awaitItem() as PrototypeUiState.Idle
            assertEquals("Red", state.clones[0].colorName)
            assertEquals("Blue", state.originalShape.colorName)
        }
    }

    @Test
    fun `modifying clone size does not affect original`() = runTest {
        viewModel.uiState.test {
            awaitItem()
            viewModel.onClone()
            awaitItem()
            viewModel.onCloneSizeChanged(0, 150)
            val state = awaitItem() as PrototypeUiState.Idle
            assertEquals(150, state.clones[0].size)
            assertEquals(100, state.originalShape.size)
        }
    }

    @Test
    fun `multiple clones are independent`() = runTest {
        viewModel.uiState.test {
            awaitItem()
            viewModel.onClone()
            awaitItem()
            viewModel.onClone()
            awaitItem()
            viewModel.onCloneColorChanged(0, "Red", 0xFFF44336)
            val state = awaitItem() as PrototypeUiState.Idle
            assertEquals(2, state.clones.size)
            assertEquals("Red", state.clones[0].colorName)
            assertEquals("Blue", state.clones[1].colorName)
        }
    }

    @Test
    fun `onOriginalTypeChanged updates original type`() = runTest {
        viewModel.uiState.test {
            awaitItem()
            viewModel.onOriginalTypeChanged(ShapeType.SQUARE)
            val state = awaitItem() as PrototypeUiState.Idle
            assertEquals(ShapeType.SQUARE, state.originalShape.type)
        }
    }
}
