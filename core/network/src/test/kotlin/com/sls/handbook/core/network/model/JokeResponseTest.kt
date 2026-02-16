package com.sls.handbook.core.network.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class JokeResponseTest {

    @Test
    fun `jokeResponse holds type, setup, punchline, and id`() {
        val response = JokeResponse(
            type = "general",
            setup = "Why did the chicken cross the road?",
            punchline = "To get to the other side",
            id = 123,
        )
        assertEquals("general", response.type)
        assertEquals("Why did the chicken cross the road?", response.setup)
        assertEquals("To get to the other side", response.punchline)
        assertEquals(123, response.id)
    }

    @Test
    fun `jokeResponses with same data are equal`() {
        val a = JokeResponse(type = "general", setup = "S", punchline = "P", id = 1)
        val b = JokeResponse(type = "general", setup = "S", punchline = "P", id = 1)
        assertEquals(a, b)
    }

    @Test
    fun `jokeResponses with different types are not equal`() {
        val a = JokeResponse(type = "general", setup = "S", punchline = "P", id = 1)
        val b = JokeResponse(type = "programming", setup = "S", punchline = "P", id = 1)
        assertNotEquals(a, b)
    }

    @Test
    fun `jokeResponses with different setups are not equal`() {
        val a = JokeResponse(type = "general", setup = "S1", punchline = "P", id = 1)
        val b = JokeResponse(type = "general", setup = "S2", punchline = "P", id = 1)
        assertNotEquals(a, b)
    }

    @Test
    fun `jokeResponses with different punchlines are not equal`() {
        val a = JokeResponse(type = "general", setup = "S", punchline = "P1", id = 1)
        val b = JokeResponse(type = "general", setup = "S", punchline = "P2", id = 1)
        assertNotEquals(a, b)
    }

    @Test
    fun `jokeResponses with different ids are not equal`() {
        val a = JokeResponse(type = "general", setup = "S", punchline = "P", id = 1)
        val b = JokeResponse(type = "general", setup = "S", punchline = "P", id = 2)
        assertNotEquals(a, b)
    }

    @Test
    fun `jokeResponse copy works correctly`() {
        val original = JokeResponse(
            type = "general",
            setup = "Original setup",
            punchline = "Original punchline",
            id = 1,
        )
        val copied = original.copy(punchline = "New punchline", id = 2)
        assertEquals("general", copied.type)
        assertEquals("Original setup", copied.setup)
        assertEquals("New punchline", copied.punchline)
        assertEquals(2, copied.id)
    }

    @Test
    fun `equal jokeResponses have same hashCode`() {
        val a = JokeResponse(type = "general", setup = "S", punchline = "P", id = 1)
        val b = JokeResponse(type = "general", setup = "S", punchline = "P", id = 1)
        assertEquals(a.hashCode(), b.hashCode())
    }

    @Test
    fun `jokeResponse can have different type values`() {
        val general = JokeResponse(type = "general", setup = "S", punchline = "P", id = 1)
        val programming = JokeResponse(type = "programming", setup = "S", punchline = "P", id = 2)
        val knockKnock = JokeResponse(type = "knock-knock", setup = "S", punchline = "P", id = 3)

        assertEquals("general", general.type)
        assertEquals("programming", programming.type)
        assertEquals("knock-knock", knockKnock.type)
    }
}
