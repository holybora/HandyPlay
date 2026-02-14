package com.sls.handbook.core.domain.repository

import com.sls.handbook.core.model.CatFacts

interface CatFactsRepository {
    suspend fun getCatFacts(ttlMillis: Long): CatFactsResult
}

data class CatFactsResult(
    val catFacts: CatFacts,
    val fetchTimeMillis: Long,
    val fromCache: Boolean,
)
