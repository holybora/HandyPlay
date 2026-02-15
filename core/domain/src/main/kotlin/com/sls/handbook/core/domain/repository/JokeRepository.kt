package com.sls.handbook.core.domain.repository

import com.sls.handbook.core.model.Joke

interface JokeRepository {
    suspend fun getJoke(ttlMillis: Long): JokeResult
}

data class JokeResult(
    val joke: Joke,
    val fetchTimeMillis: Long,
    val fromCache: Boolean,
)
