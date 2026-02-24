package com.sls.handbook.feature.dp.behavioral.command

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
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
class CommandViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: CommandViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = CommandViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state has no formatting applied`() = runTest {
        viewModel.uiState.test {
            val state = awaitItem() as CommandUiState.Idle
            assertFalse(state.isBold)
            assertFalse(state.isItalic)
            assertFalse(state.isUnderline)
            assertEquals("Hello, World!", state.text)
            assertTrue(state.undoStack.isEmpty())
        }
    }

    @Test
    fun `toggle bold adds command to undo stack`() = runTest {
        viewModel.uiState.test {
            awaitItem()
            viewModel.onToggleBold()
            val state = awaitItem() as CommandUiState.Idle
            assertTrue(state.isBold)
            assertEquals(1, state.undoStack.size)
            assertTrue(state.commandHistory.any { it.contains("Bold") })
        }
    }

    @Test
    fun `undo reverses last command`() = runTest {
        viewModel.uiState.test {
            awaitItem()
            viewModel.onToggleBold()
            awaitItem()
            viewModel.onUndo()
            val state = awaitItem() as CommandUiState.Idle
            assertFalse(state.isBold)
            assertTrue(state.undoStack.isEmpty())
            assertEquals(1, state.redoStack.size)
        }
    }

    @Test
    fun `redo reapplies undone command`() = runTest {
        viewModel.uiState.test {
            awaitItem()
            viewModel.onToggleBold()
            awaitItem()
            viewModel.onUndo()
            awaitItem()
            viewModel.onRedo()
            val state = awaitItem() as CommandUiState.Idle
            assertTrue(state.isBold)
            assertEquals(1, state.undoStack.size)
            assertTrue(state.redoStack.isEmpty())
        }
    }

    @Test
    fun `new command after undo clears redo stack`() = runTest {
        viewModel.uiState.test {
            awaitItem()
            viewModel.onToggleBold()
            awaitItem()
            viewModel.onUndo()
            awaitItem()
            viewModel.onToggleItalic()
            val state = awaitItem() as CommandUiState.Idle
            assertTrue(state.isItalic)
            assertTrue(state.redoStack.isEmpty())
        }
    }

    @Test
    fun `text change is tracked as command`() = runTest {
        viewModel.uiState.test {
            awaitItem()
            viewModel.onTextChanged("New text")
            val state = awaitItem() as CommandUiState.Idle
            assertEquals("New text", state.text)
            assertEquals(1, state.undoStack.size)
        }
    }
}
