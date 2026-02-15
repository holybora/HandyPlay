package com.sls.handbook.core.network.api

import com.sls.handbook.core.network.model.JokeResponse
import retrofit2.http.GET

interface JokeApi {
    @GET("random_joke")
    suspend fun getRandomJoke(): JokeResponse
}
