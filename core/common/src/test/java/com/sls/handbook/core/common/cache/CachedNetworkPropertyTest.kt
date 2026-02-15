package com.sls.handbook.core.common.cache

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class CachedNetworkPropertyTest {

    @Test
    fun `get returns cached value within TTL`() = runTest {
        var fetchCount = 0
        val cache = CachedNetworkProperty(ttlMillis = 60_000) {
            fetchCount++
            "data-$fetchCount"
        }

        val first = cache.get()
        val second = cache.get()

        assertEquals("data-1", first)
        assertEquals("data-1", second)
        assertEquals(1, fetchCount)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `cachedNetwork rejects zero TTL`() {
        cachedNetwork<String>(ttlMillis = 0) { "data" }
    }

    @Test(expected = IllegalArgumentException::class)
    fun `cachedNetwork rejects negative TTL`() {
        cachedNetwork<String>(ttlMillis = -1) { "data" }
    }
}
