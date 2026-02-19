package com.sls.handbook.feature.fever

import android.content.Context
import com.sls.handbook.core.model.Weather
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class WeatherMapperTest {

    private val context: Context = mockk()

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

    @Before
    fun setUp() {
        every { context.getString(R.string.fever_temperature_format, 32) } returns "32°C"
        every { context.getString(R.string.fever_temperature_format, 38) } returns "38°C"
        every { context.getString(R.string.fever_high_low_format, 35, 28) } returns "H:35° L:28°"
        every { context.getString(R.string.fever_wind_format, "4.2") } returns "4.2 m/s"
        every { context.getString(R.string.fever_humidity_format, 78) } returns "78%"
        every { context.getString(R.string.fever_pressure_format, 1008) } returns "1008 hPa"
        every { context.getString(R.string.fever_visibility_km_format, 8) } returns "8 km"
        every { context.getString(R.string.fever_unknown_location) } returns "Unknown Location"
    }

    @Test
    fun `temperature is formatted as integer with unit`() {
        val result = sampleWeather.toDisplayData(context)
        assertEquals("32°C", result.temperatureText)
    }

    @Test
    fun `icon URL is constructed from icon code`() {
        val result = sampleWeather.toDisplayData(context)
        assertEquals("https://openweathermap.org/img/wn/03d@4x.png", result.iconUrl)
    }

    @Test
    fun `blank icon produces empty URL`() {
        val result = sampleWeather.copy(icon = "").toDisplayData(context)
        assertEquals("", result.iconUrl)
    }

    @Test
    fun `icon content description matches weather description`() {
        val result = sampleWeather.toDisplayData(context)
        assertEquals("scattered clouds", result.iconContentDescription)
    }

    @Test
    fun `high low text formats max and min temperatures`() {
        val result = sampleWeather.toDisplayData(context)
        assertEquals("H:35° L:28°", result.highLowText)
    }

    @Test
    fun `wind text includes speed and unit`() {
        val result = sampleWeather.toDisplayData(context)
        assertEquals("4.2 m/s", result.windText)
    }

    @Test
    fun `humidity text includes percentage`() {
        val result = sampleWeather.toDisplayData(context)
        assertEquals("78%", result.humidityText)
    }

    @Test
    fun `location name combines city and country`() {
        val result = sampleWeather.toDisplayData(context)
        assertEquals("Surabaya, ID", result.locationName)
    }

    @Test
    fun `blank city name produces Unknown Location`() {
        val result = sampleWeather.copy(cityName = "").toDisplayData(context)
        assertEquals("Unknown Location", result.locationName)
    }

    @Test
    fun `city without country shows city only`() {
        val result = sampleWeather.copy(country = "").toDisplayData(context)
        assertEquals("Surabaya", result.locationName)
    }

    @Test
    fun `description is title-cased`() {
        val result = sampleWeather.toDisplayData(context)
        assertEquals("Scattered clouds", result.descriptionText)
    }

    @Test
    fun `feels like text is formatted with integer temperature`() {
        val result = sampleWeather.toDisplayData(context)
        assertEquals("38°C", result.feelsLikeText)
    }

    @Test
    fun `pressure text includes unit`() {
        val result = sampleWeather.toDisplayData(context)
        assertEquals("1008 hPa", result.pressureText)
    }

    @Test
    fun `visibility below 1000 shows meters`() {
        every { context.getString(R.string.fever_visibility_meters_format, 500) } returns "500 m"
        val result = sampleWeather.copy(visibility = 500).toDisplayData(context)
        assertEquals("500 m", result.visibilityText)
    }

    @Test
    fun `visibility at or above 1000 shows kilometers`() {
        val result = sampleWeather.copy(visibility = 8000).toDisplayData(context)
        assertEquals("8 km", result.visibilityText)
    }

    @Test
    fun `visibility at exactly 1000 shows 1 km`() {
        every { context.getString(R.string.fever_visibility_km_format, 1) } returns "1 km"
        val result = sampleWeather.copy(visibility = 1000).toDisplayData(context)
        assertEquals("1 km", result.visibilityText)
    }

    @Test
    fun `visibility integer division truncates fractional kilometers`() {
        every { context.getString(R.string.fever_visibility_km_format, 1) } returns "1 km"
        val result = sampleWeather.copy(visibility = 1500).toDisplayData(context)
        assertEquals("1 km", result.visibilityText)
    }

    @Test
    fun `coordinates are formatted to 4 decimal places`() {
        val result = sampleWeather.toDisplayData(context)
        assertEquals("-7.2575", result.latitudeText)
        assertEquals("112.7521", result.longitudeText)
    }
}
