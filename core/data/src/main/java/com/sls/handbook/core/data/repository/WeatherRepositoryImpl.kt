package com.sls.handbook.core.data.repository

import com.sls.handbook.core.domain.repository.WeatherRepository
import com.sls.handbook.core.model.DailyForecast
import com.sls.handbook.core.model.Weather
import com.sls.handbook.core.network.api.WeatherApi
import com.sls.handbook.core.network.model.ForecastItemResponse
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

private const val AppId = "ae103060692fe13422deb98285505dc6"
private const val LatMin = -90.0
private const val LatMax = 90.0
private const val LonMin = -180.0
private const val LonMax = 180.0
private const val MiddayHour = 12

@Singleton
class WeatherRepositoryImpl @Inject constructor(
    private val weatherApi: WeatherApi,
) : WeatherRepository {

    override suspend fun getWeatherForRandomLocation(): Weather {
        val lat = Random.nextDouble(LatMin, LatMax)
        val lon = Random.nextDouble(LonMin, LonMax)
        return fetchWeather(lat, lon)
    }

    override suspend fun getWeatherWithForecast(): Pair<Weather, List<DailyForecast>> {
        val lat = Random.nextDouble(LatMin, LatMax)
        val lon = Random.nextDouble(LonMin, LonMax)
        val weather = fetchWeather(lat, lon)
        val forecastResponse = weatherApi.getForecast(lat = lat, lon = lon, appId = AppId)
        val today = LocalDate.now(ZoneOffset.UTC)
        val forecast = forecastResponse.list
            .groupBy { item ->
                Instant.ofEpochSecond(item.dt).atZone(ZoneOffset.UTC).toLocalDate()
            }
            .filterKeys { it.isAfter(today) }
            .toSortedMap()
            .map { (_, items) -> aggregateDay(items) }
        return weather to forecast
    }

    private suspend fun fetchWeather(lat: Double, lon: Double): Weather {
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

    private fun aggregateDay(items: List<ForecastItemResponse>): DailyForecast {
        val middayItem = items.minBy { item ->
            val hour = Instant.ofEpochSecond(item.dt).atZone(ZoneOffset.UTC).hour
            kotlin.math.abs(hour - MiddayHour)
        }
        return DailyForecast(
            dateEpochSeconds = middayItem.dt,
            tempMin = items.minOf { it.main.tempMin },
            tempMax = items.maxOf { it.main.tempMax },
            icon = middayItem.weather.firstOrNull()?.icon.orEmpty(),
        )
    }
}
