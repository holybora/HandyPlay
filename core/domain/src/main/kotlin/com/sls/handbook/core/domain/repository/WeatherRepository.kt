package com.sls.handbook.core.domain.repository

import com.sls.handbook.core.model.Weather

interface WeatherRepository {
    suspend fun getWeatherForRandomLocation(): Weather
}
