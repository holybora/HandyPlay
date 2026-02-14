package com.sls.handbook.feature.home

import app.cash.turbine.test
import com.sls.handbook.core.model.Category
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class HomeViewModelTest {

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        viewModel = HomeViewModel()
    }

    @Test
    fun `initial state is Success with all six categories`() = runTest {
        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is HomeUiState.Success)
            assertEquals(6, (state as HomeUiState.Success).categories.size)
            assertEquals("", state.searchQuery)
        }
    }

    @Test
    fun `initial categories contain expected names`() = runTest {
        viewModel.uiState.test {
            val success = awaitItem() as HomeUiState.Success
            val names = success.categories.map { it.name }
            assertTrue("Kotlin Fundamentals" in names)
            assertTrue("Android Core" in names)
            assertTrue("Jetpack Compose" in names)
            assertTrue("Architecture" in names)
            assertTrue("Testing" in names)
            assertTrue("Performance" in names)
        }
    }

    @Test
    fun `search with matching query filters categories`() = runTest {
        viewModel.uiState.test {
            awaitItem() // initial state

            viewModel.onSearchQueryChanged("Kotlin")
            val filtered = awaitItem() as HomeUiState.Success
            assertEquals(1, filtered.categories.size)
            assertEquals("Kotlin Fundamentals", filtered.categories.first().name)
            assertEquals("Kotlin", filtered.searchQuery)
        }
    }

    @Test
    fun `search is case insensitive`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onSearchQueryChanged("kotlin")
            val filtered = awaitItem() as HomeUiState.Success
            assertEquals(1, filtered.categories.size)
            assertEquals("Kotlin Fundamentals", filtered.categories.first().name)
        }
    }

    @Test
    fun `search with partial match returns matching categories`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onSearchQueryChanged("Arch")
            val filtered = awaitItem() as HomeUiState.Success
            assertEquals(1, filtered.categories.size)
            assertEquals("Architecture", filtered.categories.first().name)
        }
    }

    @Test
    fun `search with no matches returns empty list`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onSearchQueryChanged("zzzzz")
            val filtered = awaitItem() as HomeUiState.Success
            assertTrue(filtered.categories.isEmpty())
            assertEquals("zzzzz", filtered.searchQuery)
        }
    }

    @Test
    fun `clearing search restores all categories`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onSearchQueryChanged("Kotlin")
            awaitItem()

            viewModel.onSearchQueryChanged("")
            val restored = awaitItem() as HomeUiState.Success
            assertEquals(6, restored.categories.size)
            assertEquals("", restored.searchQuery)
        }
    }

    @Test
    fun `blank search query restores all categories`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onSearchQueryChanged("Kotlin")
            awaitItem()

            viewModel.onSearchQueryChanged("   ")
            val restored = awaitItem() as HomeUiState.Success
            assertEquals(6, restored.categories.size)
            assertEquals("   ", restored.searchQuery)
        }
    }

    @Test
    fun `search query is preserved in state`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onSearchQueryChanged("Arch")
            val state = awaitItem() as HomeUiState.Success
            assertEquals("Arch", state.searchQuery)
        }
    }

    @Test
    fun `multiple sequential searches update state correctly`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onSearchQueryChanged("Test")
            val first = awaitItem() as HomeUiState.Success
            assertEquals(1, first.categories.size)
            assertEquals("Testing", first.categories.first().name)

            viewModel.onSearchQueryChanged("Comp")
            val second = awaitItem() as HomeUiState.Success
            assertEquals(1, second.categories.size)
            assertEquals("Jetpack Compose", second.categories.first().name)
        }
    }
}
