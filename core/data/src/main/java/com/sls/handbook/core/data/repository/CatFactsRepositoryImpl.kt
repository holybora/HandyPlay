package com.sls.handbook.core.data.repository

import com.sls.handbook.core.common.cache.DynamicTtlCache
import com.sls.handbook.core.domain.repository.CatFactsRepository
import com.sls.handbook.core.domain.repository.CatFactsResult
import com.sls.handbook.core.model.CatFacts
import com.sls.handbook.core.network.api.CatFactsApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CatFactsRepositoryImpl @Inject constructor(
    private val catFactsApi: CatFactsApi,
) : CatFactsRepository {

    private val cache = DynamicTtlCache(
        fetcher = {
            val response = catFactsApi.getFacts()
            CatFacts(facts = response.data.map { it.fact })
        },
    )

    override suspend fun getCatFacts(ttlMillis: Long): CatFactsResult {
        val result = cache.get(ttlMillis)
        return CatFactsResult(
            catFacts = result.data,
            fetchTimeMillis = result.fetchTimeMillis,
            fromCache = result.fromCache,
        )
    }
}
