package com.sls.handbook.core.network.api

import com.sls.handbook.core.network.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("data/2.5/weather")
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appId: String,
        @Query("units") units: String = "metric",
    ): WeatherResponse
}
