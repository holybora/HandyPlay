package com.sls.handbook.feature.fever

import com.sls.handbook.core.model.Weather
import java.util.Locale

private const val WeatherIconBaseUrl = "https://openweathermap.org/img/wn/"
private const val WeatherIconSuffix = "@4x.png"
private const val VisibilityThresholdMeters = 1000

internal fun Weather.toDisplayData(): WeatherDisplayData = WeatherDisplayData(
    temperatureText = "${temperature.toInt()}°C",
    iconUrl = if (icon.isNotBlank()) {
        "$WeatherIconBaseUrl$icon$WeatherIconSuffix"
    } else {
        ""
    },
    iconContentDescription = description,
    highLowText = "H:${tempMax.toInt()}° L:${tempMin.toInt()}°",
    windText = "$windSpeed m/s",
    humidityText = "$humidity%",
    locationName = buildLocationName(cityName, country),
    descriptionText = description.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
    },
    feelsLikeText = "Feels like ${feelsLike.toInt()}°C",
    pressureText = "$pressure hPa",
    visibilityText = if (visibility < VisibilityThresholdMeters) {
        "$visibility m"
    } else {
        "${visibility / VisibilityThresholdMeters} km"
    },
    latitudeText = String.format(Locale.US, "%.4f", latitude),
    longitudeText = String.format(Locale.US, "%.4f", longitude),
)

private fun buildLocationName(cityName: String, country: String): String =
    if (cityName.isNotBlank()) {
        if (country.isNotBlank()) "$cityName, $country" else cityName
    } else {
        "Unknown Location"
    }
