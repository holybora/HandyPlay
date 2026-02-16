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
                "kotlin_fundamentals" -> listOf(
                    Topic("kf_1", "Variables & Types", "kotlin_fundamentals"),
                    Topic("kf_2", "Control Flow", "kotlin_fundamentals"),
                    Topic("kf_3", "Functions", "kotlin_fundamentals"),
                )
                else -> emptyList()
            }
    }

    private fun createSavedStateHandle(
        categoryId: String = "kotlin_fundamentals",
        categoryName: String = "Kotlin Fundamentals",
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
            assertEquals("Kotlin Fundamentals", state.categoryName)
            assertEquals("", state.searchQuery)
        }
    }

    @Test
    fun `initial topics contain expected names`() = runTest {
        viewModel.uiState.test {
            val success = awaitItem() as CategoryUiState.Success
            val names = success.topics.map { it.name }
            assertTrue("Variables & Types" in names)
            assertTrue("Control Flow" in names)
            assertTrue("Functions" in names)
        }
    }

    @Test
    fun `search with matching query filters topics`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onSearchQueryChanged("Control")
            val filtered = awaitItem() as CategoryUiState.Success
            assertEquals(1, filtered.topics.size)
            assertEquals("Control Flow", filtered.topics.first().name)
            assertEquals("Control", filtered.searchQuery)
        }
    }

    @Test
    fun `search is case insensitive`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onSearchQueryChanged("variables")
            val filtered = awaitItem() as CategoryUiState.Success
            assertEquals(1, filtered.topics.size)
            assertEquals("Variables & Types", filtered.topics.first().name)
        }
    }

    @Test
    fun `search with partial match returns matching topics`() = runTest {
        viewModel.uiState.test {
            awaitItem()

            viewModel.onSearchQueryChanged("Func")
            val filtered = awaitItem() as CategoryUiState.Success
            assertEquals(1, filtered.topics.size)
            assertEquals("Functions", filtered.topics.first().name)
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

            viewModel.onSearchQueryChanged("Control")
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

            viewModel.onSearchQueryChanged("Control")
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

            viewModel.onSearchQueryChanged("Control")
            val state = awaitItem() as CategoryUiState.Success
            assertEquals("Control", state.searchQuery)
        }
    }

    @Test
    fun `category name is preserved from navigation args`() = runTest {
        viewModel.uiState.test {
            val state = awaitItem() as CategoryUiState.Success
            assertEquals("Kotlin Fundamentals", state.categoryName)
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
