package com.sls.handbook.core.model

/**
 * Single 3-hourly forecast data point from the OpenWeatherMap 5-day forecast API.
 *
 * @property dt forecast timestamp in UTC epoch seconds
 * @property temperature predicted temperature in Celsius
 * @property tempMin minimum temperature for the period, in Celsius
 * @property tempMax maximum temperature for the period, in Celsius
 * @property icon OpenWeatherMap weather icon code (e.g., "10d")
 * @property description human-readable weather condition text
 * @property pop probability of precipitation, from 0.0 (none) to 1.0 (certain)
 */
data class ForecastItem(
    val dt: Long,
    val temperature: Double,
    val tempMin: Double,
    val tempMax: Double,
    val icon: String,
    val description: String,
    val pop: Double,
)
