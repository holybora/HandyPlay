package com.sls.handbook.core.network.api

import com.sls.handbook.core.network.model.CatFactsResponse
import retrofit2.http.GET

interface CatFactsApi {
    @GET("facts")
    suspend fun getFacts(): CatFactsResponse
}
