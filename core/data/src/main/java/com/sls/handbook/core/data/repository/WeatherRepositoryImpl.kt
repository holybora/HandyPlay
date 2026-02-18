package com.sls.handbook.core.data.repository

import com.sls.handbook.core.domain.repository.WeatherRepository
import com.sls.handbook.core.model.Weather
import com.sls.handbook.core.network.api.WeatherApi
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

private const val AppId = "ae103060692fe13422deb98285505dc6"
private const val LatMin = -90.0
private const val LatMax = 90.0
private const val LonMin = -180.0
private const val LonMax = 180.0

@Singleton
class WeatherRepositoryImpl @Inject constructor(
    private val weatherApi: WeatherApi,
) : WeatherRepository {

    override suspend fun getWeatherForRandomLocation(): Weather {
        val lat = Random.nextDouble(LatMin, LatMax)
        val lon = Random.nextDouble(LonMin, LonMax)
        val response = weatherApi.getWeather(lat = lat, lon = lon, appId = AppId)
        val condition = response.weather.firstOrNull()
        return Weather(
            cityName = response.name,
            country = response.sys.country.orEmpty(),
            latitude = response.coord.lat,
            longitude = response.coord.lon,
            temperature = response.main.temp,
            feelsLike = response.main.feelsLike,
            tempMin = response.main.tempMin,
            tempMax = response.main.tempMax,
            humidity = response.main.humidity,
            pressure = response.main.pressure,
            description = condition?.description.orEmpty(),
            icon = condition?.icon.orEmpty(),
            windSpeed = response.wind.speed,
            visibility = response.visibility,
        )
    }
}
