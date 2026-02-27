package com.sls.handbook.core.data.mapper

import com.sls.handbook.core.model.ForecastData
import com.sls.handbook.core.model.ForecastItem
import com.sls.handbook.core.model.Weather
import com.sls.handbook.core.network.model.ForecastItemResponse
import com.sls.handbook.core.network.model.ForecastResponse
import com.sls.handbook.core.network.model.WeatherResponse

internal fun WeatherResponse.toDomain(): Weather {
    val condition = weather.firstOrNull()
    return Weather(
        cityName = name,
        country = sys.country.orEmpty(),
        latitude = coord.lat,
        longitude = coord.lon,
        temperature = main.temp,
        feelsLike = main.feelsLike,
        tempMin = main.tempMin,
        tempMax = main.tempMax,
        humidity = main.humidity,
        pressure = main.pressure,
        description = condition?.description.orEmpty(),
        icon = condition?.icon.orEmpty(),
        windSpeed = wind.speed,
        visibility = visibility,
    )
}

internal fun ForecastResponse.toDomain(): ForecastData =
    ForecastData(
        items = list.map { it.toDomain() },
        timezoneOffsetSeconds = city.timezone,
    )

internal fun ForecastItemResponse.toDomain(): ForecastItem {
    val condition = weather.firstOrNull()
    return ForecastItem(
        dt = dt,
        temperature = main.temp,
        tempMin = main.tempMin,
        tempMax = main.tempMax,
        icon = condition?.icon.orEmpty(),
        description = condition?.description.orEmpty(),
        pop = pop,
    )
}
