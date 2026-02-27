package com.sls.handbook.core.data.repository

import com.sls.handbook.core.common.cache.DynamicTtlCache
import com.sls.handbook.core.data.mapper.toDomain
import com.sls.handbook.core.domain.repository.JokeRepository
import com.sls.handbook.core.domain.repository.JokeResult
import com.sls.handbook.core.network.api.JokeApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JokeRepositoryImpl @Inject constructor(
    private val jokeApi: JokeApi,
) : JokeRepository {

    private val cache = DynamicTtlCache(
        fetcher = { jokeApi.getRandomJoke().toDomain() },
    )

    override suspend fun getJoke(ttlMillis: Long): JokeResult {
        val result = cache.get(ttlMillis)
        return JokeResult(
            joke = result.data,
            fetchTimeMillis = result.fetchTimeMillis,
            fromCache = result.fromCache,
        )
    }
}
