package com.sls.handbook.feature.ttlcache

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class TtlCacheUiStateTest {

    @Test
    fun `Idle has default values`() {
        val state = TtlCacheUiState.Idle()
        assertEquals(5, state.ttlSeconds)
        assertEquals("", state.lastFetchedTime)
        assertEquals("", state.data)
        assertFalse(state.isLoading)
    }

    @Test
    fun `Idle can be constructed with custom values`() {
        val state = TtlCacheUiState.Idle(
            ttlSeconds = 10,
            lastFetchedTime = "12:34:56",
            data = "Joke data",
            isLoading = true,
        )
        assertEquals(10, state.ttlSeconds)
        assertEquals("12:34:56", state.lastFetchedTime)
        assertEquals("Joke data", state.data)
        assertTrue(state.isLoading)
    }

    @Test
    fun `Idle copy preserves unchanged fields`() {
        val original = TtlCacheUiState.Idle(
            ttlSeconds = 3,
            lastFetchedTime = "10:00:00",
            data = "Original data",
            isLoading = false,
        )
        val copied = original.copy(isLoading = true)
        assertEquals(3, copied.ttlSeconds)
        assertEquals("10:00:00", copied.lastFetchedTime)
        assertEquals("Original data", copied.data)
        assertTrue(copied.isLoading)
    }

    @Test
    fun `Idle states with same data are equal`() {
        val a = TtlCacheUiState.Idle(
            ttlSeconds = 5,
            lastFetchedTime = "12:00:00",
            data = "Data",
            isLoading = false,
        )
        val b = TtlCacheUiState.Idle(
            ttlSeconds = 5,
            lastFetchedTime = "12:00:00",
            data = "Data",
            isLoading = false,
        )
        assertEquals(a, b)
    }

    @Test
    fun `Error holds message`() {
        val state = TtlCacheUiState.Error(message = "Network error")
        assertEquals("Network error", state.message)
    }

    @Test
    fun `Error states with same message are equal`() {
        val a = TtlCacheUiState.Error("Error message")
        val b = TtlCacheUiState.Error("Error message")
        assertEquals(a, b)
    }

    @Test
    fun `Idle can have different ttlSeconds values`() {
        val state1 = TtlCacheUiState.Idle(ttlSeconds = 1)
        val state2 = TtlCacheUiState.Idle(ttlSeconds = 5)
        val state3 = TtlCacheUiState.Idle(ttlSeconds = 10)

        assertEquals(1, state1.ttlSeconds)
        assertEquals(5, state2.ttlSeconds)
        assertEquals(10, state3.ttlSeconds)
    }

    @Test
    fun `Idle isLoading can be true or false`() {
        val notLoading = TtlCacheUiState.Idle(isLoading = false)
        val loading = TtlCacheUiState.Idle(isLoading = true)

        assertFalse(notLoading.isLoading)
        assertTrue(loading.isLoading)
    }

    @Test
    fun `Idle can hold joke data string`() {
        val state = TtlCacheUiState.Idle(
            data = "Setup: Why did the chicken cross the road?\n\nPunchline: To get to the other side",
        )
        assertTrue(state.data.contains("Setup:"))
        assertTrue(state.data.contains("Punchline:"))
    }

    @Test
    fun `Idle lastFetchedTime can include cache label`() {
        val cached = TtlCacheUiState.Idle(lastFetchedTime = "12:34:56 (from cache)")
        val fresh = TtlCacheUiState.Idle(lastFetchedTime = "12:34:56 (fresh)")

        assertTrue(cached.lastFetchedTime.contains("from cache"))
        assertTrue(fresh.lastFetchedTime.contains("fresh"))
    }
}
