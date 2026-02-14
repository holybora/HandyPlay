package com.sls.handbook.feature.topic

import com.sls.handbook.core.model.ContentSection
import com.sls.handbook.core.model.TopicContent

internal object TopicContentProvider {

    fun getContent(topicId: String, topicName: String): TopicContent? =
        contentMap[topicId]?.let { sections ->
            TopicContent(
                topicId = topicId,
                topicName = topicName,
                sections = sections,
            )
        }

    private val contentMap: Map<String, List<ContentSection>> = mapOf(
        "kf_7" to ttlCachedSections(),
    )
}

private fun ttlCachedSections(): List<ContentSection> = listOf(
    cachedNetworkPropertySection(),
    provideDelegateSection(),
    lazyCompositionSection(),
)

private fun cachedNetworkPropertySection() = ContentSection(
    title = "CachedNetworkProperty",
    description = "A ReadOnlyProperty delegate that caches a network-fetched value " +
        "with a configurable TTL. Uses Mutex for coroutine-safe double-checked " +
        "locking and @Volatile fields for cross-thread visibility.",
    code = """
class CachedNetworkProperty<T>(
    private val ttlMillis: Long,
    private val fetcher: suspend () -> T
) : ReadOnlyProperty<Any?, T> {

    @Volatile private var cachedValue: T? = null
    @Volatile private var lastFetchTime: Long = 0L
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
                    if (nowInner - lastFetchTime < ttlMillis)
                        return@runBlocking value
                }
                val fresh = fetcher()
                cachedValue = fresh
                lastFetchTime = nowInner
                fresh
            }
        }
    }
}
    """.trimIndent(),
)

private fun provideDelegateSection() = ContentSection(
    title = "provideDelegate Validation",
    description = "provideDelegate is called at property initialization time, " +
        "before the delegate is assigned. Invalid configurations fail fast at " +
        "construction time rather than at first access.",
    code = """
class CachedNetworkPropertyProvider<T>(
    private val ttlMillis: Long,
    private val fetcher: suspend () -> T
) {
    operator fun provideDelegate(
        thisRef: Any?,
        property: KProperty<*>
    ): ReadOnlyProperty<Any?, T> {
        require(ttlMillis > 0) {
            "TTL for '${"$"}{property.name}' must be positive"
        }
        require(!property.returnType.isMarkedNullable) {
            "CachedNetworkProperty does not support " +
                "nullable types for '${"$"}{property.name}'"
        }
        return CachedNetworkProperty(ttlMillis, fetcher)
    }
}

fun <T> cachedNetwork(
    ttlMillis: Long,
    fetcher: suspend () -> T
) = CachedNetworkPropertyProvider(ttlMillis, fetcher)

// Usage
class UserRepository {
    val currentUser: User by cachedNetwork(
        ttlMillis = 60_000
    ) {
        api.fetchCurrentUser()
    }
}
    """.trimIndent(),
)

private fun lazyCompositionSection() = ContentSection(
    title = "Lazy Composition",
    description = "You cannot directly compose lazy and a custom delegate on the " +
        "same property. You can compose them internally: lazy ensures the " +
        "CachedNetworkProperty is not created until first access. lazy provides " +
        "one-time initialization; the TTL cache provides periodic refresh.",
    code = """
class LazyCachedNetworkProperty<T>(
    ttlMillis: Long,
    fetcher: suspend () -> T
) : ReadOnlyProperty<Any?, T> {

    private val innerDelegate by lazy {
        CachedNetworkProperty(ttlMillis, fetcher)
    }

    override fun getValue(
        thisRef: Any?,
        property: KProperty<*>
    ): T {
        return innerDelegate.getValue(thisRef, property)
    }
}
    """.trimIndent(),
)
