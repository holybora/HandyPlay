package com.sls.handbook.feature.home

import com.sls.handbook.core.model.Category
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class HomeUiStateTest {

    @Test
    fun `Loading is a singleton`() {
        val a: HomeUiState = HomeUiState.Loading
        val b: HomeUiState = HomeUiState.Loading
        assertTrue(a === b)
    }

    @Test
    fun `Success holds categories and search query`() {
        val categories = listOf(Category(id = "1", name = "Test"))
        val state = HomeUiState.Success(categories = categories, searchQuery = "query")
        assertEquals(categories, state.categories)
        assertEquals("query", state.searchQuery)
    }

    @Test
    fun `Success has empty search query by default`() {
        val state = HomeUiState.Success(categories = emptyList())
        assertEquals("", state.searchQuery)
    }

    @Test
    fun `Success copy preserves unchanged fields`() {
        val categories = listOf(Category(id = "1", name = "A"))
        val original = HomeUiState.Success(categories = categories, searchQuery = "old")
        val copied = original.copy(searchQuery = "new")
        assertEquals("new", copied.searchQuery)
        assertEquals(categories, copied.categories)
    }

    @Test
    fun `Error holds message`() {
        val state = HomeUiState.Error(message = "Something went wrong")
        assertEquals("Something went wrong", state.message)
    }

    @Test
    fun `Success states with same data are equal`() {
        val categories = listOf(Category(id = "1", name = "A"))
        val a = HomeUiState.Success(categories = categories, searchQuery = "x")
        val b = HomeUiState.Success(categories = categories, searchQuery = "x")
        assertEquals(a, b)
    }

    @Test
    fun `Error states with same message are equal`() {
        val a = HomeUiState.Error("err")
        val b = HomeUiState.Error("err")
        assertEquals(a, b)
    }
}
