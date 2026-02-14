package com.sls.handbook.core.common.cache

import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.reflect.KProperty

class CachedNetworkPropertyTest {

    private val testProp: String = "stub"

    @Test
    fun `property delegate returns cached value within TTL`() {
        var fetchCount = 0
        val property = CachedNetworkProperty(ttlMillis = 60_000) {
            fetchCount++
            "data-$fetchCount"
        }

        val kProperty: KProperty<*> = this::testProp

        val first = property.getValue(null, kProperty)
        val second = property.getValue(null, kProperty)

        assertEquals("data-1", first)
        assertEquals("data-1", second)
        assertEquals(1, fetchCount)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `provider rejects zero TTL`() {
        val provider = CachedNetworkPropertyProvider<String>(ttlMillis = 0) { "data" }
        val kProperty: KProperty<*> = this::testProp
        provider.provideDelegate(null, kProperty)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `provider rejects negative TTL`() {
        val provider = CachedNetworkPropertyProvider<String>(ttlMillis = -1) { "data" }
        val kProperty: KProperty<*> = this::testProp
        provider.provideDelegate(null, kProperty)
    }
}
