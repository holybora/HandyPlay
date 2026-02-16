package com.sls.handbook.ui

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BottomSearchBarModelsTest {

    @Test
    fun `BottomSearchBarUiState has correct defaults`() {
        val state = BottomSearchBarUiState()
        assertFalse(state.isVisible)
        assertTrue(state.pathSegments.isEmpty())
        assertEquals("", state.searchQuery)
    }

    @Test
    fun `BottomSearchBarUiState can be constructed with custom values`() {
        val state = BottomSearchBarUiState(
            isVisible = true,
            pathSegments = listOf("Home", "Category"),
            searchQuery = "test query",
        )
        assertTrue(state.isVisible)
        assertEquals(listOf("Home", "Category"), state.pathSegments)
        assertEquals("test query", state.searchQuery)
    }

    @Test
    fun `BottomSearchBarUiState copy preserves unchanged fields`() {
        val original = BottomSearchBarUiState(
            isVisible = true,
            pathSegments = listOf("Home"),
            searchQuery = "old",
        )
        val copied = original.copy(searchQuery = "new")
        assertTrue(copied.isVisible)
        assertEquals(listOf("Home"), copied.pathSegments)
        assertEquals("new", copied.searchQuery)
    }

    @Test
    fun `BottomSearchBarUiState states with same data are equal`() {
        val a = BottomSearchBarUiState(
            isVisible = true,
            pathSegments = listOf("A"),
            searchQuery = "q",
        )
        val b = BottomSearchBarUiState(
            isVisible = true,
            pathSegments = listOf("A"),
            searchQuery = "q",
        )
        assertEquals(a, b)
    }

    @Test
    fun `CurrentScreen Home is instantiable`() {
        val screen: CurrentScreen = CurrentScreen.Home
        assertEquals(CurrentScreen.Home, screen)
    }

    @Test
    fun `CurrentScreen Category holds categoryName`() {
        val screen = CurrentScreen.Category(categoryName = "Kotlin Fundamentals")
        assertEquals("Kotlin Fundamentals", screen.categoryName)
    }

    @Test
    fun `CurrentScreen Category instances with same name are equal`() {
        val a = CurrentScreen.Category("Name")
        val b = CurrentScreen.Category("Name")
        assertEquals(a, b)
    }

    @Test
    fun `CurrentScreen Other is instantiable`() {
        val screen: CurrentScreen = CurrentScreen.Other
        assertEquals(CurrentScreen.Other, screen)
    }

    @Test
    fun `all CurrentScreen types are distinct`() {
        val home: CurrentScreen = CurrentScreen.Home
        val category: CurrentScreen = CurrentScreen.Category("Test")
        val other: CurrentScreen = CurrentScreen.Other

        // Verify they're different types
        assertTrue(home is CurrentScreen.Home)
        assertTrue(category is CurrentScreen.Category)
        assertTrue(other is CurrentScreen.Other)
    }

    @Test
    fun `BottomSearchBarEvent NavigateToHome is instantiable`() {
        val event: BottomSearchBarEvent = BottomSearchBarEvent.NavigateToHome
        assertEquals(BottomSearchBarEvent.NavigateToHome, event)
    }

    @Test
    fun `BottomSearchBarEvent NavigateToHome is singleton`() {
        val a = BottomSearchBarEvent.NavigateToHome
        val b = BottomSearchBarEvent.NavigateToHome
        assertTrue(a === b)
    }

    @Test
    fun `CurrentScreen Home is singleton`() {
        val a = CurrentScreen.Home
        val b = CurrentScreen.Home
        assertTrue(a === b)
    }

    @Test
    fun `CurrentScreen Other is singleton`() {
        val a = CurrentScreen.Other
        val b = CurrentScreen.Other
        assertTrue(a === b)
    }

    @Test
    fun `BottomSearchBarUiState can have multiple path segments`() {
        val state = BottomSearchBarUiState(
            pathSegments = listOf("Home", "Kotlin Fundamentals", "Coroutines"),
        )
        assertEquals(3, state.pathSegments.size)
        assertEquals("Home", state.pathSegments[0])
        assertEquals("Kotlin Fundamentals", state.pathSegments[1])
        assertEquals("Coroutines", state.pathSegments[2])
    }

    @Test
    fun `BottomSearchBarUiState isVisible can be toggled`() {
        val hidden = BottomSearchBarUiState(isVisible = false)
        val visible = BottomSearchBarUiState(isVisible = true)

        assertFalse(hidden.isVisible)
        assertTrue(visible.isVisible)
    }
}
