package com.sls.handbook.feature.fever

import com.sls.handbook.core.model.HourlyForecast
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class HourlyDisplayDataMapperTest {

    private val stringResolver = object : StringResolver {
        override fun getString(resId: Int, vararg args: Any): String = when (resId) {
            R.string.fever_temperature_format -> "${args[0]}°C"
            R.string.fever_hourly_pop_format -> "${args[0]}%"
            else -> ""
        }
    }

    private val sampleForecast = HourlyForecast(
        dt = 1_700_000_000L,
        temperature = 28.7,
        icon = "10d",
        description = "light rain",
        pop = 0.32,
    )

    @Test
    fun `temperature is formatted as integer with celsius unit`() {
        val result = sampleForecast.toHourlyDisplayData(stringResolver)
        assertEquals("28°C", result.temperatureText)
    }

    @Test
    fun `temperature is truncated to integer not rounded`() {
        val result = sampleForecast.copy(temperature = 28.9).toHourlyDisplayData(stringResolver)
        assertEquals("28°C", result.temperatureText)
    }

    @Test
    fun `icon URL uses 2x suffix for compact display`() {
        val result = sampleForecast.toHourlyDisplayData(stringResolver)
        assertTrue(result.iconUrl.endsWith("@2x.png"))
        assertTrue(result.iconUrl.contains("10d"))
    }

    @Test
    fun `blank icon produces empty URL`() {
        val result = sampleForecast.copy(icon = "").toHourlyDisplayData(stringResolver)
        assertEquals("", result.iconUrl)
    }

    @Test
    fun `pop is converted to integer percentage`() {
        val result = sampleForecast.toHourlyDisplayData(stringResolver)
        assertEquals("32%", result.popText)
    }

    @Test
    fun `zero pop shows 0 percent`() {
        val result = sampleForecast.copy(pop = 0.0).toHourlyDisplayData(stringResolver)
        assertEquals("0%", result.popText)
    }

    @Test
    fun `full pop shows 100 percent`() {
        val result = sampleForecast.copy(pop = 1.0).toHourlyDisplayData(stringResolver)
        assertEquals("100%", result.popText)
    }

    @Test
    fun `time text is not blank`() {
        val result = sampleForecast.toHourlyDisplayData(stringResolver)
        assertTrue(result.timeText.isNotBlank())
    }
}
