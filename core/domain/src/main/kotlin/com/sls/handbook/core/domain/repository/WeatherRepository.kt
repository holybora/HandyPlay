package com.sls.handbook.core.domain.repository

import com.sls.handbook.core.model.DailyForecast
import com.sls.handbook.core.model.Weather

interface WeatherRepository {
    suspend fun getWeatherForRandomLocation(): Weather
    suspend fun getWeatherWithForecast(): Pair<Weather, List<DailyForecast>>
}
