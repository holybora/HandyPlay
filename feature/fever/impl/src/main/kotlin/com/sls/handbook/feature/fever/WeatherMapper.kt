package com.sls.handbook.feature.fever

import com.sls.handbook.core.model.DailyForecast
import com.sls.handbook.core.model.HourlyForecast
import com.sls.handbook.core.model.Weather
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.time.format.TextStyle as TimeTextStyle

private const val WeatherIconBaseUrl = "https://openweathermap.org/img/wn/"
private const val WeatherIconSuffix = "@4x.png"
private const val ForecastIconSuffix = "@2x.png"
private const val WeatherIconSmallSuffix = "@2x.png"
private const val VisibilityThresholdMeters = 1000
private const val PopPercentageMultiplier = 100

internal fun Weather.toDisplayData(
    stringResolver: StringResolver,
    dailyForecast: List<DailyForecast> = emptyList(),
    hourlyForecasts: List<HourlyForecast> = emptyList(),
): WeatherDisplayData =
    WeatherDisplayData(
        temperatureText = stringResolver.getString(
            R.string.fever_temperature_format,
            temperature.toInt(),
        ),
        iconUrl = if (icon.isNotBlank()) {
            "$WeatherIconBaseUrl$icon$WeatherIconSuffix"
        } else {
            ""
        },
        iconContentDescription = description,
        highLowText = stringResolver.getString(
            R.string.fever_high_low_format,
            tempMax.toInt(),
            tempMin.toInt(),
        ),
        windText = stringResolver.getString(R.string.fever_wind_format, windSpeed.toString()),
        humidityText = stringResolver.getString(R.string.fever_humidity_format, humidity),
        locationName = buildLocationName(stringResolver, cityName, country),
        descriptionText = description.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
        },
        feelsLikeText = stringResolver.getString(
            R.string.fever_temperature_format,
            feelsLike.toInt(),
        ),
        pressureText = stringResolver.getString(R.string.fever_pressure_format, pressure),
        visibilityText = if (visibility < VisibilityThresholdMeters) {
            stringResolver.getString(R.string.fever_visibility_meters_format, visibility)
        } else {
            stringResolver.getString(
                R.string.fever_visibility_km_format,
                visibility / VisibilityThresholdMeters,
            )
        },
        latitudeText = String.format(Locale.US, "%.4f", latitude),
        longitudeText = String.format(Locale.US, "%.4f", longitude),
        fiveDaysForecast = dailyForecast.map { it.toDisplayData(stringResolver) },
        hourlyForecasts = hourlyForecasts.map { it.toHourlyDisplayData(stringResolver) },
    )

internal fun DailyForecast.toDisplayData(stringResolver: StringResolver): DailyForecastDisplayData {
    val dayName = Instant.ofEpochSecond(dateEpochSeconds)
        .atZone(ZoneId.systemDefault())
        .dayOfWeek
        .getDisplayName(TimeTextStyle.SHORT, Locale.getDefault())

    return DailyForecastDisplayData(
        dayName = dayName,
        iconUrl = if (icon.isNotBlank()) {
            "$WeatherIconBaseUrl$icon$ForecastIconSuffix"
        } else {
            ""
        },
        highText = stringResolver.getString(R.string.fever_forecast_high_format, tempMax.toInt()),
        lowText = stringResolver.getString(R.string.fever_forecast_low_format, tempMin.toInt()),
    )
}

internal fun HourlyForecast.toHourlyDisplayData(stringResolver: StringResolver): HourlyDisplayData {
    val timeFormatter = DateTimeFormatter.ofPattern("h a", Locale.getDefault())
    val timeText = Instant.ofEpochSecond(dt)
        .atZone(ZoneId.systemDefault())
        .format(timeFormatter)
    return HourlyDisplayData(
        timeText = timeText,
        iconUrl = if (icon.isNotBlank()) "$WeatherIconBaseUrl$icon$WeatherIconSmallSuffix" else "",
        temperatureText = stringResolver.getString(
            R.string.fever_temperature_format,
            temperature.toInt(),
        ),
        popText = stringResolver.getString(
            R.string.fever_hourly_pop_format,
            (pop * PopPercentageMultiplier).toInt(),
        ),
    )
}

private fun buildLocationName(
    stringResolver: StringResolver,
    cityName: String,
    country: String,
): String =
    if (cityName.isNotBlank()) {
        if (country.isNotBlank()) "$cityName, $country" else cityName
    } else {
        stringResolver.getString(R.string.fever_unknown_location)
    }
