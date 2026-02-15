package com.sls.handbook.core.common.cache

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class DynamicTtlCache<T>(
    private val fetcher: suspend () -> T,
) {
    @Volatile
    private var cachedValue: T? = null

    @Volatile
    private var lastFetchTime: Long = 0L

    private val mutex = Mutex()

    suspend fun get(ttlMillis: Long): CacheResult<T> {
        val now = System.currentTimeMillis()
        cachedValue?.let { value ->
            if (now - lastFetchTime < ttlMillis) {
                return CacheResult(value, lastFetchTime, fromCache = true)
            }
        }
        return mutex.withLock {
            val nowInner = System.currentTimeMillis()
            cachedValue?.let { value ->
                if (nowInner - lastFetchTime < ttlMillis) {
                    return@withLock CacheResult(value, lastFetchTime, fromCache = true)
                }
            }
            val fresh = fetcher()
            cachedValue = fresh
            lastFetchTime = nowInner
            CacheResult(fresh, nowInner, fromCache = false)
        }
    }

    fun invalidate() {
        cachedValue = null
        lastFetchTime = 0L
    }
}

data class CacheResult<T>(
    val data: T,
    val fetchTimeMillis: Long,
    val fromCache: Boolean,
)
