package com.sls.handbook.core.network.api

import com.sls.handbook.core.network.model.PicsumImageResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PicsumApi {
    @GET("v2/list")
    suspend fun getImages(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
    ): List<PicsumImageResponse>
}
