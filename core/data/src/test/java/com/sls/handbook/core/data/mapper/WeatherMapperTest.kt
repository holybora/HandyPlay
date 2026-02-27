package com.sls.handbook.core.data.mapper

import com.sls.handbook.core.network.model.CityResponse
import com.sls.handbook.core.network.model.CoordResponse
import com.sls.handbook.core.network.model.ForecastItemResponse
import com.sls.handbook.core.network.model.ForecastResponse
import com.sls.handbook.core.network.model.MainResponse
import com.sls.handbook.core.network.model.SysResponse
import com.sls.handbook.core.network.model.WeatherConditionResponse
import com.sls.handbook.core.network.model.WeatherResponse
import com.sls.handbook.core.network.model.WindResponse
import org.junit.Assert.assertEquals
import org.junit.Test

class WeatherMapperTest {

    private val sampleResponse = WeatherResponse(
        coord = CoordResponse(lat = -7.25, lon = 112.75),
        weather = listOf(
            WeatherConditionResponse(
                main = "Clouds",
                description = "scattered clouds",
                icon = "03d",
            ),
        ),
        main = MainResponse(
            temp = 32.5,
            feelsLike = 38.0,
            tempMin = 28.0,
            tempMax = 35.0,
            pressure = 1008,
            humidity = 78,
        ),
        visibility = 8000,
        wind = WindResponse(speed = 4.2),
        sys = SysResponse(country = "ID"),
        name = "Surabaya",
    )

    @Test
    fun `WeatherResponse toDomain maps all fields`() {
        val result = sampleResponse.toDomain()

        assertEquals("Surabaya", result.cityName)
        assertEquals("ID", result.country)
        assertEquals(-7.25, result.latitude, 0.001)
        assertEquals(112.75, result.longitude, 0.001)
        assertEquals(32.5, result.temperature, 0.001)
        assertEquals(38.0, result.feelsLike, 0.001)
        assertEquals(28.0, result.tempMin, 0.001)
        assertEquals(35.0, result.tempMax, 0.001)
        assertEquals(78, result.humidity)
        assertEquals(1008, result.pressure)
        assertEquals("scattered clouds", result.description)
        assertEquals("03d", result.icon)
        assertEquals(4.2, result.windSpeed, 0.001)
        assertEquals(8000, result.visibility)
    }

    @Test
    fun `WeatherResponse toDomain defaults description and icon when weather list is empty`() {
        val response = sampleResponse.copy(weather = emptyList())

        val result = response.toDomain()

        assertEquals("", result.description)
        assertEquals("", result.icon)
    }

    @Test
    fun `WeatherResponse toDomain defaults country when null`() {
        val response = sampleResponse.copy(sys = SysResponse(country = null))

        val result = response.toDomain()

        assertEquals("", result.country)
    }

    @Test
    fun `ForecastItemResponse toDomain maps all fields`() {
        val item = ForecastItemResponse(
            dt = 1700000000L,
            main = MainResponse(
                temp = 30.0,
                feelsLike = 33.0,
                tempMin = 27.0,
                tempMax = 32.0,
                pressure = 1010,
                humidity = 65,
            ),
            weather = listOf(
                WeatherConditionResponse(main = "Rain", description = "light rain", icon = "10d"),
            ),
            pop = 0.8,
        )

        val result = item.toDomain()

        assertEquals(1700000000L, result.dt)
        assertEquals(30.0, result.temperature, 0.001)
        assertEquals(27.0, result.tempMin, 0.001)
        assertEquals(32.0, result.tempMax, 0.001)
        assertEquals("10d", result.icon)
        assertEquals("light rain", result.description)
        assertEquals(0.8, result.pop, 0.001)
    }

    @Test
    fun `ForecastItemResponse toDomain defaults description and icon when weather list is empty`() {
        val item = ForecastItemResponse(
            dt = 1700000000L,
            main = MainResponse(
                temp = 30.0,
                feelsLike = 33.0,
                tempMin = 27.0,
                tempMax = 32.0,
                pressure = 1010,
                humidity = 65,
            ),
            weather = emptyList(),
            pop = 0.5,
        )

        val result = item.toDomain()

        assertEquals("", result.icon)
        assertEquals("", result.description)
    }

    @Test
    fun `ForecastResponse toDomain maps list and timezone`() {
        val items = listOf(
            ForecastItemResponse(
                dt = 1700000000L,
                main = MainResponse(
                    temp = 30.0,
                    feelsLike = 33.0,
                    tempMin = 27.0,
                    tempMax = 32.0,
                    pressure = 1010,
                    humidity = 65,
                ),
                weather = listOf(
                    WeatherConditionResponse(
                        main = "Clear",
                        description = "clear sky",
                        icon = "01d",
                    ),
                ),
                pop = 0.0,
            ),
            ForecastItemResponse(
                dt = 1700010800L,
                main = MainResponse(
                    temp = 28.0,
                    feelsLike = 30.0,
                    tempMin = 25.0,
                    tempMax = 29.0,
                    pressure = 1012,
                    humidity = 70,
                ),
                weather = listOf(
                    WeatherConditionResponse(
                        main = "Rain",
                        description = "light rain",
                        icon = "10n",
                    ),
                ),
                pop = 0.6,
            ),
        )
        val response = ForecastResponse(
            list = items,
            city = CityResponse(timezone = 25200),
        )

        val result = response.toDomain()

        assertEquals(2, result.items.size)
        assertEquals(25200, result.timezoneOffsetSeconds)
        assertEquals(1700000000L, result.items[0].dt)
        assertEquals(1700010800L, result.items[1].dt)
    }
}
