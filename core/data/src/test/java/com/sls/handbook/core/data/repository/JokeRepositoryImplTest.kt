package com.sls.handbook.core.data.repository

import com.sls.handbook.core.network.api.JokeApi
import com.sls.handbook.core.network.model.JokeResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.IOException

class JokeRepositoryImplTest {

    private lateinit var jokeApi: JokeApi
    private lateinit var repository: JokeRepositoryImpl

    @Before
    fun setUp() {
        jokeApi = mockk()
        repository = JokeRepositoryImpl(jokeApi)
    }

    @Test
    fun `getJoke returns joke from API on first call`() = runTest {
        val apiResponse = JokeResponse(
            type = "general",
            setup = "Why did the chicken cross the road?",
            punchline = "To get to the other side",
            id = 123,
        )
        coEvery { jokeApi.getRandomJoke() } returns apiResponse

        val result = repository.getJoke(ttlMillis = 60_000)

        assertEquals("Why did the chicken cross the road?", result.joke.setup)
        assertEquals("To get to the other side", result.joke.punchline)
        assertFalse(result.fromCache)
        assertTrue(result.fetchTimeMillis > 0)
        coVerify(exactly = 1) { jokeApi.getRandomJoke() }
    }

    @Test
    fun `getJoke maps API response to domain model`() = runTest {
        val apiResponse = JokeResponse(
            type = "programming",
            setup = "Why do programmers prefer dark mode?",
            punchline = "Because light attracts bugs",
            id = 456,
        )
        coEvery { jokeApi.getRandomJoke() } returns apiResponse

        val result = repository.getJoke(ttlMillis = 60_000)

        assertEquals("Why do programmers prefer dark mode?", result.joke.setup)
        assertEquals("Because light attracts bugs", result.joke.punchline)
    }

    @Test
    fun `getJoke returns cached result within TTL`() = runTest {
        val apiResponse = JokeResponse(
            type = "general",
            setup = "Setup",
            punchline = "Punchline",
            id = 1,
        )
        coEvery { jokeApi.getRandomJoke() } returns apiResponse

        // First call - fresh
        val firstResult = repository.getJoke(ttlMillis = 60_000)
        assertFalse(firstResult.fromCache)

        // Second call within TTL - cached
        val secondResult = repository.getJoke(ttlMillis = 60_000)
        assertTrue(secondResult.fromCache)
        assertEquals(firstResult.joke, secondResult.joke)
        assertEquals(firstResult.fetchTimeMillis, secondResult.fetchTimeMillis)

        // API should only be called once
        coVerify(exactly = 1) { jokeApi.getRandomJoke() }
    }

    @Test
    fun `getJoke fetches fresh data with different TTL values`() = runTest {
        val response1 = JokeResponse(type = "t", setup = "S1", punchline = "P1", id = 1)
        val response2 = JokeResponse(type = "t", setup = "S2", punchline = "P2", id = 2)
        coEvery { jokeApi.getRandomJoke() } returnsMany listOf(response1, response2)

        // First call with 1 second TTL
        val result1 = repository.getJoke(ttlMillis = 1000)
        assertEquals("S1", result1.joke.setup)
        assertFalse(result1.fromCache)

        // Second call with 0 TTL (expired immediately) - should fetch fresh
        val result2 = repository.getJoke(ttlMillis = 0)
        assertEquals("S2", result2.joke.setup)
        assertFalse(result2.fromCache)

        coVerify(exactly = 2) { jokeApi.getRandomJoke() }
    }

    @Test
    fun `getJoke propagates API exceptions`() = runTest {
        coEvery { jokeApi.getRandomJoke() } throws IOException("Network error")

        try {
            repository.getJoke(ttlMillis = 60_000)
            throw AssertionError("Expected IOException to be thrown")
        } catch (e: IOException) {
            assertEquals("Network error", e.message)
        }
    }

    @Test
    fun `getJoke includes correct fetch time metadata`() = runTest {
        val apiResponse = JokeResponse(
            type = "general",
            setup = "Setup",
            punchline = "Punchline",
            id = 1,
        )
        coEvery { jokeApi.getRandomJoke() } returns apiResponse

        val beforeFetch = System.currentTimeMillis()
        val result = repository.getJoke(ttlMillis = 60_000)
        val afterFetch = System.currentTimeMillis()

        assertTrue(result.fetchTimeMillis >= beforeFetch)
        assertTrue(result.fetchTimeMillis <= afterFetch)
    }
}
