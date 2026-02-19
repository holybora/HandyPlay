package com.sls.handbook.core.network.api

import com.sls.handbook.core.network.model.HourlyForecastResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface HourlyForecastApi {

    @GET("data/2.5/forecast")
    suspend fun getHourlyForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appId: String,
        @Query("units") units: String = "metric",
    ): HourlyForecastResponse
}
