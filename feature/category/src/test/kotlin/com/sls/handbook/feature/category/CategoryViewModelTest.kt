package com.sls.handbook.feature.category

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.sls.handbook.core.domain.repository.CategoryRepository
import com.sls.handbook.core.model.Category
import com.sls.handbook.core.model.Topic
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CategoryViewModelTest {

    private lateinit var viewModel: CategoryViewModel

    private val fakeRepository = object : CategoryRepository {
        override fun getCategories(): List<Category> = emptyList()

        override fun getTopicsByCategoryId(categoryId: String): List<Topic> =
            when (categoryId) {
                "design_patterns" -> listOf(
                    Topic.DesignPattern.FactoryMethod,
                    Topic.DesignPattern.Observer,
                    Topic.DesignPattern.Strategy,
                )
                else -> emptyList()
            }
    }

    private fun createSavedStateHandle(
        categoryId: String = "design_patterns",
        categoryName: String = "Design Patterns",
    ): SavedStateHandle = SavedStateHandle(
        mapOf(
            "categoryId" to categoryId,
            "categoryName" to categoryName,
        ),
    )

    @Before
    fun setUp() {
        viewModel = CategoryViewModel(
            savedStateHandle = createSavedStateHandle(),
            categoryRepository = fakeRepository,
        )
    }

    @Test
    fun `initial state is Success with all topics`() = runTest {
        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state is CategoryUiState.Success)
            assertEquals(3, (state as CategoryUiState.Success).topics.size)
            assertEquals("Design Patterns", state.categoryName)
            assertEquals("", state.searchQuery)
        }
    }

    @Test
    fun `initial topics contain expected names`() = runTest {
        viewModel.uiState.test {
            val success = awaitItem() as CategoryUiState.Success
            val names = success.topics.map { it.name }
            assertTrue("Factory Method" in names)
            assertTrue("Observer" in names)
            assertTrue("Strategy" in names)
        }
    }

    @Test
    fun `search with matching query filters topics`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onSearchQueryChanged("Observer")
            val filtered = awaitItem() as CategoryUiState.Success
            assertEquals(1, filtered.topics.size)
            assertEquals("Observer", filtered.topics.first().name)
            assertEquals("Observer", filtered.searchQuery)
        }
    }

    @Test
    fun `search is case insensitive`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onSearchQueryChanged("factory")
            val filtered = awaitItem() as CategoryUiState.Success
            assertEquals(1, filtered.topics.size)
            assertEquals("Factory Method", filtered.topics.first().name)
        }
    }

    @Test
    fun `search with partial match returns matching topics`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onSearchQueryChanged("Strat")
            val filtered = awaitItem() as CategoryUiState.Success
            assertEquals(1, filtered.topics.size)
            assertEquals("Strategy", filtered.topics.first().name)
        }
    }

    @Test
    fun `search with no matches returns empty list`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onSearchQueryChanged("zzzzz")
            val filtered = awaitItem() as CategoryUiState.Success
            assertTrue(filtered.topics.isEmpty())
            assertEquals("zzzzz", filtered.searchQuery)
        }
    }

    @Test
    fun `clearing search restores all topics`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onSearchQueryChanged("Observer")
            awaitItem()

            viewModel.onSearchQueryChanged("")
            val restored = awaitItem() as CategoryUiState.Success
            assertEquals(3, restored.topics.size)
            assertEquals("", restored.searchQuery)
        }
    }

    @Test
    fun `blank search query restores all topics`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onSearchQueryChanged("Observer")
            awaitItem()

            viewModel.onSearchQueryChanged("   ")
            val restored = awaitItem() as CategoryUiState.Success
            assertEquals(3, restored.topics.size)
            assertEquals("   ", restored.searchQuery)
        }
    }

    @Test
    fun `search query is preserved in state`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onSearchQueryChanged("Observer")
            val state = awaitItem() as CategoryUiState.Success
            assertEquals("Observer", state.searchQuery)
        }
    }

    @Test
    fun `category name is preserved from navigation args`() = runTest {
        viewModel.uiState.test {
            val state = awaitItem() as CategoryUiState.Success
            assertEquals("Design Patterns", state.categoryName)
        }
    }

    @Test
    fun `unknown category returns empty topics`() = runTest {
        val vm = CategoryViewModel(
            savedStateHandle = createSavedStateHandle(
                categoryId = "unknown",
                categoryName = "Unknown",
            ),
            categoryRepository = fakeRepository,
        )
        vm.uiState.test {
            val state = awaitItem() as CategoryUiState.Success
            assertTrue(state.topics.isEmpty())
            assertEquals("Unknown", state.categoryName)
        }
    }
}
