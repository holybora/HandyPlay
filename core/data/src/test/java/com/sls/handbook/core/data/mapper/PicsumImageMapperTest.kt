package com.sls.handbook.core.data.mapper

import com.sls.handbook.core.network.model.PicsumImageResponse
import org.junit.Assert.assertEquals
import org.junit.Test

class PicsumImageMapperTest {

    @Test
    fun `toDomain maps all fields correctly`() {
        val response = PicsumImageResponse(
            id = "42",
            author = "John Doe",
            width = 1920,
            height = 1080,
            url = "https://unsplash.com/photos/42",
            downloadUrl = "https://picsum.photos/id/42/1920/1080",
        )

        val result = response.toDomain()

        assertEquals("42", result.id)
        assertEquals("John Doe", result.author)
        assertEquals(1920, result.width)
        assertEquals(1080, result.height)
        assertEquals("https://picsum.photos/id/42/1920/1080", result.downloadUrl)
    }
}
