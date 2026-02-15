package com.sls.handbook.core.common.cache

class CachedNetworkPropertyProvider<T>(
    private val ttlMillis: Long,
    private val fetcher: suspend () -> T,
) {
    fun create(): CachedNetworkProperty<T> {
        require(ttlMillis > 0) {
            "TTL must be positive, was $ttlMillis"
        }
        return CachedNetworkProperty(ttlMillis, fetcher)
    }
}

fun <T> cachedNetwork(ttlMillis: Long, fetcher: suspend () -> T): CachedNetworkProperty<T> {
    require(ttlMillis > 0) {
        "TTL must be positive, was $ttlMillis"
    }
    return CachedNetworkProperty(ttlMillis, fetcher)
}
