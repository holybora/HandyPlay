package com.sls.handbook.core.common.cache

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class DynamicTtlCacheTest {

    @Test
    fun `first call fetches fresh data`() = runTest {
        var fetchCount = 0
        val cache = DynamicTtlCache {
            fetchCount++
            "data-$fetchCount"
        }

        val result = cache.get(ttlMillis = 60_000)

        assertEquals("data-1", result.data)
        assertFalse(result.fromCache)
        assertEquals(1, fetchCount)
    }

    @Test
    fun `second call within TTL returns cached data`() = runTest {
        var fetchCount = 0
        val cache = DynamicTtlCache {
            fetchCount++
            "data-$fetchCount"
        }

        cache.get(ttlMillis = 60_000)
        val result = cache.get(ttlMillis = 60_000)

        assertEquals("data-1", result.data)
        assertTrue(result.fromCache)
        assertEquals(1, fetchCount)
    }

    @Test
    fun `invalidate forces fresh fetch`() = runTest {
        var fetchCount = 0
        val cache = DynamicTtlCache {
            fetchCount++
            "data-$fetchCount"
        }

        cache.get(ttlMillis = 60_000)
        cache.invalidate()
        val result = cache.get(ttlMillis = 60_000)

        assertEquals("data-2", result.data)
        assertFalse(result.fromCache)
        assertEquals(2, fetchCount)
    }

    @Test
    fun `cache result includes fetch time`() = runTest {
        val cache = DynamicTtlCache { "data" }

        val result = cache.get(ttlMillis = 60_000)

        assertTrue(result.fetchTimeMillis > 0)
    }
}
