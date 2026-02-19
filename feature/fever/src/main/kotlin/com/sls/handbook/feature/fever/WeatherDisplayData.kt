package com.sls.handbook.feature.fever

data class DailyForecastDisplayData(
    val dayName: String,
    val iconUrl: String,
    val highText: String,
    val lowText: String,
)

data class WeatherDisplayData(
    val temperatureText: String,
    val iconUrl: String,
    val iconContentDescription: String,
    val highLowText: String,
    val windText: String,
    val humidityText: String,
    val locationName: String,
    val descriptionText: String,
    val feelsLikeText: String,
    val pressureText: String,
    val visibilityText: String,
    val latitudeText: String,
    val longitudeText: String,
    val forecast: List<DailyForecastDisplayData>,
) {
    companion object {
        fun empty() = WeatherDisplayData(
            temperatureText = "",
            iconUrl = "",
            iconContentDescription = "",
            highLowText = "",
            windText = "",
            humidityText = "",
            locationName = "",
            descriptionText = "",
            feelsLikeText = "",
            pressureText = "",
            visibilityText = "",
            latitudeText = "",
            longitudeText = "",
            forecast = emptyList(),
        )
    }
}
