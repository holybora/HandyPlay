package com.sls.handbook.feature.fever

import com.sls.handbook.core.model.Weather
import org.junit.Assert.assertEquals
import org.junit.Test

class WeatherMapperTest {

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
        val result = sampleWeather.toDisplayData()
        assertEquals("32°C", result.temperatureText)
    }

    @Test
    fun `icon URL is constructed from icon code`() {
        val result = sampleWeather.toDisplayData()
        assertEquals("https://openweathermap.org/img/wn/03d@4x.png", result.iconUrl)
    }

    @Test
    fun `blank icon produces empty URL`() {
        val result = sampleWeather.copy(icon = "").toDisplayData()
        assertEquals("", result.iconUrl)
    }

    @Test
    fun `icon content description matches weather description`() {
        val result = sampleWeather.toDisplayData()
        assertEquals("scattered clouds", result.iconContentDescription)
    }

    @Test
    fun `high low text formats max and min temperatures`() {
        val result = sampleWeather.toDisplayData()
        assertEquals("H:35° L:28°", result.highLowText)
    }

    @Test
    fun `wind text includes speed and unit`() {
        val result = sampleWeather.toDisplayData()
        assertEquals("4.2 m/s", result.windText)
    }

    @Test
    fun `humidity text includes percentage`() {
        val result = sampleWeather.toDisplayData()
        assertEquals("78%", result.humidityText)
    }

    @Test
    fun `location name combines city and country`() {
        val result = sampleWeather.toDisplayData()
        assertEquals("Surabaya, ID", result.locationName)
    }

    @Test
    fun `blank city name produces Unknown Location`() {
        val result = sampleWeather.copy(cityName = "").toDisplayData()
        assertEquals("Unknown Location", result.locationName)
    }

    @Test
    fun `city without country shows city only`() {
        val result = sampleWeather.copy(country = "").toDisplayData()
        assertEquals("Surabaya", result.locationName)
    }

    @Test
    fun `description is title-cased`() {
        val result = sampleWeather.toDisplayData()
        assertEquals("Scattered clouds", result.descriptionText)
    }

    @Test
    fun `feels like text is formatted with integer temperature`() {
        val result = sampleWeather.toDisplayData()
        assertEquals("38°C", result.feelsLikeText)
    }

    @Test
    fun `pressure text includes unit`() {
        val result = sampleWeather.toDisplayData()
        assertEquals("1008 hPa", result.pressureText)
    }

    @Test
    fun `visibility below 1000 shows meters`() {
        val result = sampleWeather.copy(visibility = 500).toDisplayData()
        assertEquals("500 m", result.visibilityText)
    }

    @Test
    fun `visibility at or above 1000 shows kilometers`() {
        val result = sampleWeather.copy(visibility = 8000).toDisplayData()
        assertEquals("8 km", result.visibilityText)
    }

    @Test
    fun `visibility at exactly 1000 shows 1 km`() {
        val result = sampleWeather.copy(visibility = 1000).toDisplayData()
        assertEquals("1 km", result.visibilityText)
    }

    @Test
    fun `visibility integer division truncates fractional kilometers`() {
        val result = sampleWeather.copy(visibility = 1500).toDisplayData()
        assertEquals("1 km", result.visibilityText)
    }

    @Test
    fun `coordinates are formatted to 4 decimal places`() {
        val result = sampleWeather.toDisplayData()
        assertEquals("-7.2575", result.latitudeText)
        assertEquals("112.7521", result.longitudeText)
    }
}
