package com.sls.handbook.core.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class JokeTest {

    @Test
    fun `joke holds setup and punchline`() {
        val joke = Joke(setup = "Why did the chicken cross the road?", punchline = "To get to the other side")
        assertEquals("Why did the chicken cross the road?", joke.setup)
        assertEquals("To get to the other side", joke.punchline)
    }

    @Test
    fun `jokes with same data are equal`() {
        val a = Joke(setup = "Setup", punchline = "Punchline")
        val b = Joke(setup = "Setup", punchline = "Punchline")
        assertEquals(a, b)
    }

    @Test
    fun `jokes with different setups are not equal`() {
        val a = Joke(setup = "Setup 1", punchline = "Punchline")
        val b = Joke(setup = "Setup 2", punchline = "Punchline")
        assertNotEquals(a, b)
    }

    @Test
    fun `jokes with different punchlines are not equal`() {
        val a = Joke(setup = "Setup", punchline = "Punchline 1")
        val b = Joke(setup = "Setup", punchline = "Punchline 2")
        assertNotEquals(a, b)
    }

    @Test
    fun `joke copy works correctly`() {
        val original = Joke(setup = "Original setup", punchline = "Original punchline")
        val copied = original.copy(punchline = "New punchline")
        assertEquals("Original setup", copied.setup)
        assertEquals("New punchline", copied.punchline)
    }

    @Test
    fun `equal jokes have same hashCode`() {
        val a = Joke(setup = "Setup", punchline = "Punchline")
        val b = Joke(setup = "Setup", punchline = "Punchline")
        assertEquals(a.hashCode(), b.hashCode())
    }
}
