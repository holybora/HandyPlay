package com.sls.handbook.core.common.cache

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class CachedNetworkProperty<T>(
    private val ttlMillis: Long,
    private val fetcher: suspend () -> T,
) : ReadOnlyProperty<Any?, T> {

    @Volatile
    private var cachedValue: T? = null

    @Volatile
    private var lastFetchTime: Long = 0L

    private val mutex = Mutex()

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        val now = System.currentTimeMillis()
        cachedValue?.let { value ->
            if (now - lastFetchTime < ttlMillis) return value
        }
        return runBlocking {
            mutex.withLock {
                // Double-check after acquiring lock
                val nowInner = System.currentTimeMillis()
                cachedValue?.let { value ->
                    if (nowInner - lastFetchTime < ttlMillis) return@runBlocking value
                }
                val fresh = fetcher()
                cachedValue = fresh
                lastFetchTime = nowInner
                fresh
            }
        }
    }
}
