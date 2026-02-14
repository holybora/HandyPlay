package com.sls.handbook.core.common.cache

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class CachedNetworkPropertyProvider<T>(
    private val ttlMillis: Long,
    private val fetcher: suspend () -> T,
) {
    operator fun provideDelegate(
        thisRef: Any?,
        property: KProperty<*>,
    ): ReadOnlyProperty<Any?, T> {
        require(ttlMillis > 0) {
            "TTL for property '${property.name}' must be positive"
        }
        require(!property.returnType.isMarkedNullable) {
            "CachedNetworkProperty does not support nullable types for '${property.name}'"
        }
        return CachedNetworkProperty(ttlMillis, fetcher)
    }
}

fun <T> cachedNetwork(ttlMillis: Long, fetcher: suspend () -> T) =
    CachedNetworkPropertyProvider(ttlMillis, fetcher)
