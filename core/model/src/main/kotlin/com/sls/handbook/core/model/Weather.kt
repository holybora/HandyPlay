package com.sls.handbook.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Weather(
    val cityName: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
    val temperature: Double,
    val feelsLike: Double,
    val tempMin: Double,
    val tempMax: Double,
    val humidity: Int,
    val pressure: Int,
    val description: String,
    val icon: String,
    val windSpeed: Double,
    val visibility: Int,
)
