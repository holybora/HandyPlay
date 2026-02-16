package com.sls.handbook.core.domain.repository

import com.sls.handbook.core.model.Joke
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class JokeResultTest {

    @Test
    fun `jokeResult holds joke, fetchTimeMillis, and fromCache`() {
        val joke = Joke(setup = "Setup", punchline = "Punchline")
        val result = JokeResult(
            joke = joke,
            fetchTimeMillis = 1_700_000_000_000L,
            fromCache = true,
        )
        assertEquals(joke, result.joke)
        assertEquals(1_700_000_000_000L, result.fetchTimeMillis)
        assertTrue(result.fromCache)
    }

    @Test
    fun `jokeResults with same data are equal`() {
        val joke = Joke(setup = "S", punchline = "P")
        val a = JokeResult(joke = joke, fetchTimeMillis = 1000L, fromCache = true)
        val b = JokeResult(joke = joke, fetchTimeMillis = 1000L, fromCache = true)
        assertEquals(a, b)
    }

    @Test
    fun `jokeResults with different jokes are not equal`() {
        val joke1 = Joke(setup = "S1", punchline = "P1")
        val joke2 = Joke(setup = "S2", punchline = "P2")
        val a = JokeResult(joke = joke1, fetchTimeMillis = 1000L, fromCache = true)
        val b = JokeResult(joke = joke2, fetchTimeMillis = 1000L, fromCache = true)
        assertNotEquals(a, b)
    }

    @Test
    fun `jokeResults with different fetchTimeMillis are not equal`() {
        val joke = Joke(setup = "S", punchline = "P")
        val a = JokeResult(joke = joke, fetchTimeMillis = 1000L, fromCache = true)
        val b = JokeResult(joke = joke, fetchTimeMillis = 2000L, fromCache = true)
        assertNotEquals(a, b)
    }

    @Test
    fun `jokeResults with different fromCache are not equal`() {
        val joke = Joke(setup = "S", punchline = "P")
        val a = JokeResult(joke = joke, fetchTimeMillis = 1000L, fromCache = true)
        val b = JokeResult(joke = joke, fetchTimeMillis = 1000L, fromCache = false)
        assertNotEquals(a, b)
    }

    @Test
    fun `jokeResult copy works correctly`() {
        val joke = Joke(setup = "Original", punchline = "Punchline")
        val original = JokeResult(joke = joke, fetchTimeMillis = 1000L, fromCache = false)
        val copied = original.copy(fromCache = true)
        assertEquals(joke, copied.joke)
        assertEquals(1000L, copied.fetchTimeMillis)
        assertTrue(copied.fromCache)
    }

    @Test
    fun `equal jokeResults have same hashCode`() {
        val joke = Joke(setup = "S", punchline = "P")
        val a = JokeResult(joke = joke, fetchTimeMillis = 1000L, fromCache = true)
        val b = JokeResult(joke = joke, fetchTimeMillis = 1000L, fromCache = true)
        assertEquals(a.hashCode(), b.hashCode())
    }

    @Test
    fun `fromCache flag can be true`() {
        val result = JokeResult(
            joke = Joke("S", "P"),
            fetchTimeMillis = 1000L,
            fromCache = true,
        )
        assertTrue(result.fromCache)
    }

    @Test
    fun `fromCache flag can be false`() {
        val result = JokeResult(
            joke = Joke("S", "P"),
            fetchTimeMillis = 1000L,
            fromCache = false,
        )
        assertFalse(result.fromCache)
    }
}
