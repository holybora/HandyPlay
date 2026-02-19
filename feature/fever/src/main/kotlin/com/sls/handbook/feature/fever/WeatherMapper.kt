package com.sls.handbook.feature.fever

import android.content.Context
import com.sls.handbook.core.model.Weather
import java.util.Locale

private const val WeatherIconBaseUrl = "https://openweathermap.org/img/wn/"
private const val WeatherIconSuffix = "@4x.png"
private const val VisibilityThresholdMeters = 1000

internal fun Weather.toDisplayData(context: Context): WeatherDisplayData = WeatherDisplayData(
    temperatureText = context.getString(R.string.fever_temperature_format, temperature.toInt()),
    iconUrl = if (icon.isNotBlank()) {
        "$WeatherIconBaseUrl$icon$WeatherIconSuffix"
    } else {
        ""
    },
    iconContentDescription = description,
    highLowText = context.getString(
        R.string.fever_high_low_format,
        tempMax.toInt(),
        tempMin.toInt(),
    ),
    windText = context.getString(R.string.fever_wind_format, windSpeed.toString()),
    humidityText = context.getString(R.string.fever_humidity_format, humidity),
    locationName = buildLocationName(context, cityName, country),
    descriptionText = description.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
    },
    feelsLikeText = context.getString(R.string.fever_temperature_format, feelsLike.toInt()),
    pressureText = context.getString(R.string.fever_pressure_format, pressure),
    visibilityText = if (visibility < VisibilityThresholdMeters) {
        context.getString(R.string.fever_visibility_meters_format, visibility)
    } else {
        context.getString(R.string.fever_visibility_km_format, visibility / VisibilityThresholdMeters)
    },
    latitudeText = String.format(Locale.US, "%.4f", latitude),
    longitudeText = String.format(Locale.US, "%.4f", longitude),
)

private fun buildLocationName(context: Context, cityName: String, country: String): String =
    if (cityName.isNotBlank()) {
        if (country.isNotBlank()) "$cityName, $country" else cityName
    } else {
        context.getString(R.string.fever_unknown_location)
    }
