package com.sls.handbook.core.common.cache

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class CachedNetworkProperty<T>(
    private val ttlMillis: Long,
    private val fetcher: suspend () -> T,
) {

    @Volatile
    private var cachedValue: T? = null

    @Volatile
    private var lastFetchTime: Long = 0L

    private val mutex = Mutex()

    @Suppress("ReturnCount")
    suspend fun get(): T {
        val now = System.currentTimeMillis()
        cachedValue?.let { value ->
            if (now - lastFetchTime < ttlMillis) return value
        }
        return mutex.withLock {
            // Double-check after acquiring lock
            val nowInner = System.currentTimeMillis()
            cachedValue?.let { value ->
                if (nowInner - lastFetchTime < ttlMillis) return value
            }
            val fresh = fetcher()
            cachedValue = fresh
            lastFetchTime = nowInner
            fresh
        }
    }
}
