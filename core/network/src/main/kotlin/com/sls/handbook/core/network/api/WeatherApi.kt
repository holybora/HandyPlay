package com.sls.handbook.core.network.api

import com.sls.handbook.core.network.model.ForecastResponse
import com.sls.handbook.core.network.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit service interface for the OpenWeatherMap REST API.
 *
 * All endpoints use metric units and support language localization via the `lang` parameter.
 */
interface WeatherApi {

    @GET("data/2.5/weather")
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String,
    ): WeatherResponse

    @GET("data/2.5/forecast")
    suspend fun getForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String,
    ): ForecastResponse
}
