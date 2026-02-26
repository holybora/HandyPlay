package com.sls.handbook.core.domain.repository

import com.sls.handbook.core.model.ForecastData
import com.sls.handbook.core.model.Weather

interface WeatherRepository {
    suspend fun getWeather(lat: Double, lon: Double, lang: String): Weather
    suspend fun getForecastData(lat: Double, lon: Double, lang: String): ForecastData
}
