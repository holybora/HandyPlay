package com.sls.handbook.feature.fever

import com.sls.handbook.core.model.DailyForecast
import com.sls.handbook.core.model.Weather
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WeatherMapperTest {

    private val stringResolver = object : StringResolver {
        override fun getString(resId: Int, vararg args: Any): String = when (resId) {
            R.string.fever_temperature_format -> "${args[0]}°C"
            R.string.fever_high_low_format -> "H:${args[0]}° L:${args[1]}°"
            R.string.fever_wind_format -> "${args[0]} m/s"
            R.string.fever_humidity_format -> "${args[0]}%"
            R.string.fever_pressure_format -> "${args[0]} hPa"
            R.string.fever_visibility_meters_format -> "${args[0]} m"
            R.string.fever_visibility_km_format -> "${args[0]} km"
            R.string.fever_unknown_location -> "Unknown Location"
            R.string.fever_forecast_high_format -> "${args[0]}°"
            R.string.fever_forecast_low_format -> "${args[0]}°"
            else -> ""
        }
    }

    private val sampleWeather = Weather(
        cityName = "Surabaya",
        country = "ID",
        latitude = -7.2575,
        longitude = 112.7521,
        temperature = 32.5,
        feelsLike = 38.0,
        tempMin = 28.0,
        tempMax = 35.0,
        humidity = 78,
        pressure = 1008,
        description = "scattered clouds",
        icon = "03d",
        windSpeed = 4.2,
        visibility = 8000,
    )

    @Test
    fun `temperature is formatted as integer with unit`() {
        val result = sampleWeather.toDisplayData(stringResolver)
        assertEquals("32°C", result.temperatureText)
    }

    @Test
    fun `icon URL is constructed from icon code`() {
        val result = sampleWeather.toDisplayData(stringResolver)
        assertEquals("https://openweathermap.org/img/wn/03d@4x.png", result.iconUrl)
    }

    @Test
    fun `blank icon produces empty URL`() {
        val result = sampleWeather.copy(icon = "").toDisplayData(stringResolver)
        assertEquals("", result.iconUrl)
    }

    @Test
    fun `icon content description matches weather description`() {
        val result = sampleWeather.toDisplayData(stringResolver)
        assertEquals("scattered clouds", result.iconContentDescription)
    }

    @Test
    fun `high low text formats max and min temperatures`() {
        val result = sampleWeather.toDisplayData(stringResolver)
        assertEquals("H:35° L:28°", result.highLowText)
    }

    @Test
    fun `wind text includes speed and unit`() {
        val result = sampleWeather.toDisplayData(stringResolver)
        assertEquals("4.2 m/s", result.windText)
    }

    @Test
    fun `humidity text includes percentage`() {
        val result = sampleWeather.toDisplayData(stringResolver)
        assertEquals("78%", result.humidityText)
    }

    @Test
    fun `location name combines city and country`() {
        val result = sampleWeather.toDisplayData(stringResolver)
        assertEquals("Surabaya, ID", result.locationName)
    }

    @Test
    fun `blank city name produces Unknown Location`() {
        val result = sampleWeather.copy(cityName = "").toDisplayData(stringResolver)
        assertEquals("Unknown Location", result.locationName)
    }

    @Test
    fun `city without country shows city only`() {
        val result = sampleWeather.copy(country = "").toDisplayData(stringResolver)
        assertEquals("Surabaya", result.locationName)
    }

    @Test
    fun `description is title-cased`() {
        val result = sampleWeather.toDisplayData(stringResolver)
        assertEquals("Scattered clouds", result.descriptionText)
    }

    @Test
    fun `feels like text is formatted with integer temperature`() {
        val result = sampleWeather.toDisplayData(stringResolver)
        assertEquals("38°C", result.feelsLikeText)
    }

    @Test
    fun `pressure text includes unit`() {
        val result = sampleWeather.toDisplayData(stringResolver)
        assertEquals("1008 hPa", result.pressureText)
    }

    @Test
    fun `visibility below 1000 shows meters`() {
        val result = sampleWeather.copy(visibility = 500).toDisplayData(stringResolver)
        assertEquals("500 m", result.visibilityText)
    }

    @Test
    fun `visibility at or above 1000 shows kilometers`() {
        val result = sampleWeather.copy(visibility = 8000).toDisplayData(stringResolver)
        assertEquals("8 km", result.visibilityText)
    }

    @Test
    fun `visibility at exactly 1000 shows 1 km`() {
        val result = sampleWeather.copy(visibility = 1000).toDisplayData(stringResolver)
        assertEquals("1 km", result.visibilityText)
    }

    @Test
    fun `visibility integer division truncates fractional kilometers`() {
        val result = sampleWeather.copy(visibility = 1500).toDisplayData(stringResolver)
        assertEquals("1 km", result.visibilityText)
    }

    @Test
    fun `coordinates are formatted to 4 decimal places`() {
        val result = sampleWeather.toDisplayData(stringResolver)
        assertEquals("-7.2575", result.latitudeText)
        assertEquals("112.7521", result.longitudeText)
    }

    // --- Forecast mapper tests ---

    @Test
    fun `default forecast parameter produces empty list`() {
        val result = sampleWeather.toDisplayData(stringResolver)
        assertEquals(emptyList<DailyForecastDisplayData>(), result.fiveDaysForecast)
    }

    @Test
    fun `forecast maps empty list to empty list`() {
        val result = sampleWeather.toDisplayData(stringResolver, emptyList())
        assertEquals(emptyList<DailyForecastDisplayData>(), result.fiveDaysForecast)
    }

    @Test
    fun `forecast day name is derived from epoch`() {
        val monday = DailyForecast(
            dateEpochSeconds = 1739145600, // 2025-02-10, a Monday
            tempMin = 20.0,
            tempMax = 30.0,
            icon = "01d",
        )
        val result = sampleWeather.toDisplayData(stringResolver, listOf(monday))
        assertTrue(result.fiveDaysForecast[0].dayName.isNotBlank())
    }

    @Test
    fun `forecast icon URL uses 2x suffix`() {
        val forecast = DailyForecast(
            dateEpochSeconds = 1739145600,
            tempMin = 20.0,
            tempMax = 30.0,
            icon = "10d",
        )
        val result = sampleWeather.toDisplayData(stringResolver, listOf(forecast))
        assertEquals("https://openweathermap.org/img/wn/10d@2x.png", result.fiveDaysForecast[0].iconUrl)
    }

    @Test
    fun `forecast blank icon produces empty URL`() {
        val forecast = DailyForecast(
            dateEpochSeconds = 1739145600,
            tempMin = 20.0,
            tempMax = 30.0,
            icon = "",
        )
        val result = sampleWeather.toDisplayData(stringResolver, listOf(forecast))
        assertEquals("", result.fiveDaysForecast[0].iconUrl)
    }

    @Test
    fun `forecast high and low are formatted as integers`() {
        val forecast = DailyForecast(
            dateEpochSeconds = 1739145600,
            tempMin = 18.7,
            tempMax = 29.3,
            icon = "01d",
        )
        val result = sampleWeather.toDisplayData(stringResolver, listOf(forecast))
        assertEquals("29°", result.fiveDaysForecast[0].highText)
        assertEquals("18°", result.fiveDaysForecast[0].lowText)
    }

    @Test
    fun `forecast list preserves order`() {
        val forecasts = (0..2).map { i ->
            DailyForecast(
                dateEpochSeconds = 1739145600L + (i * 86400),
                tempMin = 20.0 + i,
                tempMax = 30.0 + i,
                icon = "01d",
            )
        }
        val result = sampleWeather.toDisplayData(stringResolver, forecasts)
        assertEquals(3, result.fiveDaysForecast.size)
        assertEquals("20°", result.fiveDaysForecast[0].lowText)
        assertEquals("22°", result.fiveDaysForecast[2].lowText)
    }
}
