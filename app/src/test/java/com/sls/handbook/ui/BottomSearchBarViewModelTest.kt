package com.sls.handbook.ui

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
class BottomSearchBarViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: BottomSearchBarViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = BottomSearchBarViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is not visible with empty query`() = runTest {
        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isVisible)
            assertEquals(emptyList<String>(), state.pathSegments)
            assertEquals("", state.searchQuery)
        }
    }

    @Test
    fun `onDestinationChanged to Home shows bar with Home breadcrumb`() = runTest {
        viewModel.uiState.test {
            awaitItem() // initial

            viewModel.onDestinationChanged(CurrentScreen.Home)
            val state = awaitItem()
            assertTrue(state.isVisible)
            assertEquals(listOf("Home"), state.pathSegments)
            assertEquals("", state.searchQuery)
        }
    }

    @Test
    fun `onDestinationChanged to Category shows bar with category breadcrumb`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onDestinationChanged(CurrentScreen.Category("Kotlin Fundamentals"))
            val state = awaitItem()
            assertTrue(state.isVisible)
            assertEquals(listOf("Home", "Kotlin Fundamentals"), state.pathSegments)
            assertEquals("", state.searchQuery)
        }
    }

    @Test
    fun `onDestinationChanged to Other hides bar`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onDestinationChanged(CurrentScreen.Home)
            awaitItem()

            viewModel.onDestinationChanged(CurrentScreen.Other)
            val state = awaitItem()
            assertFalse(state.isVisible)
            assertEquals(emptyList<String>(), state.pathSegments)
        }
    }

    @Test
    fun `onSearchQueryChanged updates query`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onSearchQueryChanged("kotlin")
            val state = awaitItem()
            assertEquals("kotlin", state.searchQuery)
        }
    }

    @Test
    fun `onDestinationChanged clears search query`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onDestinationChanged(CurrentScreen.Home)
            awaitItem()

            viewModel.onSearchQueryChanged("kotlin")
            awaitItem()

            viewModel.onDestinationChanged(CurrentScreen.Category("Android Core"))
            val state = awaitItem()
            assertEquals("", state.searchQuery)
            assertEquals(listOf("Home", "Android Core"), state.pathSegments)
        }
    }

    @Test
    fun `onSegmentClick 0 emits NavigateToHome event`() = runTest {
        viewModel.navigationEvents.test {
            viewModel.onSegmentClick(0)
            val event = awaitItem()
            assertEquals(BottomSearchBarEvent.NavigateToHome, event)
        }
    }

    @Test
    fun `rapid destination changes maintain correct state`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onDestinationChanged(CurrentScreen.Home)
            val homeState = awaitItem()
            assertTrue(homeState.isVisible)
            assertEquals(listOf("Home"), homeState.pathSegments)

            viewModel.onDestinationChanged(CurrentScreen.Category("Testing"))
            val categoryState = awaitItem()
            assertTrue(categoryState.isVisible)
            assertEquals(listOf("Home", "Testing"), categoryState.pathSegments)

            viewModel.onDestinationChanged(CurrentScreen.Home)
            val backState = awaitItem()
            assertTrue(backState.isVisible)
            assertEquals(listOf("Home"), backState.pathSegments)
        }
    }

    @Test
    fun `onSegmentClick with non-zero index does not emit event`() = runTest {
        viewModel.navigationEvents.test {
            viewModel.onSegmentClick(1)
            expectNoEvents()
        }
    }
}
