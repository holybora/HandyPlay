package com.sls.handbook.core.common.cache

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class CachedNetworkPropertyProviderTest {

    @Test
    fun `create returns CachedNetworkProperty with valid TTL`() = runTest {
        val provider = CachedNetworkPropertyProvider(
            ttlMillis = 60_000,
            fetcher = { "data" },
        )

        val property = provider.create()

        assertNotNull(property)
        val result = property.get()
        assertEquals("data", result)
    }

    @Test
    fun `create with positive TTL succeeds`() {
        val provider = CachedNetworkPropertyProvider(
            ttlMillis = 1,
            fetcher = { "data" },
        )

        val property = provider.create()

        assertNotNull(property)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `create with zero TTL throws IllegalArgumentException`() {
        val provider = CachedNetworkPropertyProvider(
            ttlMillis = 0,
            fetcher = { "data" },
        )

        provider.create()
    }

    @Test(expected = IllegalArgumentException::class)
    fun `create with negative TTL throws IllegalArgumentException`() {
        val provider = CachedNetworkPropertyProvider(
            ttlMillis = -1,
            fetcher = { "data" },
        )

        provider.create()
    }

    @Test(expected = IllegalArgumentException::class)
    fun `create with large negative TTL throws IllegalArgumentException`() {
        val provider = CachedNetworkPropertyProvider(
            ttlMillis = -999_999,
            fetcher = { "data" },
        )

        provider.create()
    }

    @Test
    fun `cachedNetwork function with valid TTL succeeds`() = runTest {
        val property = cachedNetwork(ttlMillis = 60_000) { "test-data" }

        assertNotNull(property)
        val result = property.get()
        assertEquals("test-data", result)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `cachedNetwork function with zero TTL throws IllegalArgumentException`() {
        cachedNetwork<String>(ttlMillis = 0) { "data" }
    }

    @Test(expected = IllegalArgumentException::class)
    fun `cachedNetwork function with negative TTL throws IllegalArgumentException`() {
        cachedNetwork<String>(ttlMillis = -1) { "data" }
    }

    @Test
    fun `provider can be created with different data types`() {
        val stringProvider = CachedNetworkPropertyProvider(
            ttlMillis = 1000,
            fetcher = { "string" },
        )
        val intProvider = CachedNetworkPropertyProvider(
            ttlMillis = 1000,
            fetcher = { 42 },
        )
        val listProvider = CachedNetworkPropertyProvider(
            ttlMillis = 1000,
            fetcher = { listOf(1, 2, 3) },
        )

        assertNotNull(stringProvider.create())
        assertNotNull(intProvider.create())
        assertNotNull(listProvider.create())
    }
}
