package com.sls.handbook.feature.category

import com.sls.handbook.core.model.Topic
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CategoryUiStateTest {

    @Test
    fun `Loading is a singleton`() {
        val a: CategoryUiState = CategoryUiState.Loading
        val b: CategoryUiState = CategoryUiState.Loading
        assertTrue(a === b)
    }

    @Test
    fun `Success holds categoryName, topics, and searchQuery`() {
        val topics = listOf(Topic(id = "1", name = "Test Topic", categoryId = "cat1"))
        val state = CategoryUiState.Success(
            categoryName = "Test Category",
            topics = topics,
            searchQuery = "query",
        )
        assertEquals("Test Category", state.categoryName)
        assertEquals(topics, state.topics)
        assertEquals("query", state.searchQuery)
    }

    @Test
    fun `Success has empty search query by default`() {
        val state = CategoryUiState.Success(
            categoryName = "Category",
            topics = emptyList(),
        )
        assertEquals("", state.searchQuery)
    }

    @Test
    fun `Success copy preserves unchanged fields`() {
        val topics = listOf(Topic(id = "1", name = "Topic", categoryId = "cat"))
        val original = CategoryUiState.Success(
            categoryName = "Category",
            topics = topics,
            searchQuery = "old",
        )
        val copied = original.copy(searchQuery = "new")
        assertEquals("new", copied.searchQuery)
        assertEquals("Category", copied.categoryName)
        assertEquals(topics, copied.topics)
    }

    @Test
    fun `Success copy can update topics`() {
        val originalTopics = listOf(Topic(id = "1", name = "Topic1", categoryId = "cat"))
        val newTopics = listOf(Topic(id = "2", name = "Topic2", categoryId = "cat"))
        val original = CategoryUiState.Success(
            categoryName = "Category",
            topics = originalTopics,
        )
        val copied = original.copy(topics = newTopics)
        assertEquals(newTopics, copied.topics)
        assertEquals("Category", copied.categoryName)
    }

    @Test
    fun `Error holds message`() {
        val state = CategoryUiState.Error(message = "Failed to load topics")
        assertEquals("Failed to load topics", state.message)
    }

    @Test
    fun `Success states with same data are equal`() {
        val topics = listOf(Topic(id = "1", name = "A", categoryId = "cat"))
        val a = CategoryUiState.Success(
            categoryName = "Cat",
            topics = topics,
            searchQuery = "x",
        )
        val b = CategoryUiState.Success(
            categoryName = "Cat",
            topics = topics,
            searchQuery = "x",
        )
        assertEquals(a, b)
    }

    @Test
    fun `Error states with same message are equal`() {
        val a = CategoryUiState.Error("err")
        val b = CategoryUiState.Error("err")
        assertEquals(a, b)
    }

    @Test
    fun `Success can hold empty topics list`() {
        val state = CategoryUiState.Success(
            categoryName = "Empty Category",
            topics = emptyList(),
        )
        assertTrue(state.topics.isEmpty())
        assertEquals("Empty Category", state.categoryName)
    }

    @Test
    fun `Success can hold multiple topics`() {
        val topics = listOf(
            Topic(id = "1", name = "Topic 1", categoryId = "cat"),
            Topic(id = "2", name = "Topic 2", categoryId = "cat"),
            Topic(id = "3", name = "Topic 3", categoryId = "cat"),
        )
        val state = CategoryUiState.Success(
            categoryName = "Category",
            topics = topics,
        )
        assertEquals(3, state.topics.size)
    }
}
