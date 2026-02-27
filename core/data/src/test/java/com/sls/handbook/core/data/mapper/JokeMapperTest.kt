package com.sls.handbook.core.data.mapper

import com.sls.handbook.core.network.model.JokeResponse
import org.junit.Assert.assertEquals
import org.junit.Test

class JokeMapperTest {

    @Test
    fun `toDomain maps setup and punchline`() {
        val response = JokeResponse(
            type = "programming",
            setup = "Why do programmers prefer dark mode?",
            punchline = "Because light attracts bugs",
            id = 42,
        )

        val result = response.toDomain()

        assertEquals("Why do programmers prefer dark mode?", result.setup)
        assertEquals("Because light attracts bugs", result.punchline)
    }
}
