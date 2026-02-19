package com.sls.handbook.core.network.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("coord") val coord: CoordResponse,
    @SerializedName("weather") val weather: List<WeatherConditionResponse>,
    @SerializedName("main") val main: MainResponse,
    @SerializedName("visibility") val visibility: Int,
    @SerializedName("wind") val wind: WindResponse,
    @SerializedName("sys") val sys: SysResponse,
    @SerializedName("name") val name: String,
)

data class CoordResponse(
    @SerializedName("lat") val lat: Double,
    @SerializedName("lon") val lon: Double,
)

data class WeatherConditionResponse(
    @SerializedName("main") val main: String,
    @SerializedName("description") val description: String,
    @SerializedName("icon") val icon: String,
)

data class MainResponse(
    @SerializedName("temp") val temp: Double,
    @SerializedName("feels_like") val feelsLike: Double,
    @SerializedName("temp_min") val tempMin: Double,
    @SerializedName("temp_max") val tempMax: Double,
    @SerializedName("pressure") val pressure: Int,
    @SerializedName("humidity") val humidity: Int,
)

data class WindResponse(
    @SerializedName("speed") val speed: Double,
)

data class SysResponse(
    @SerializedName("country") val country: String?,
)

data class ForecastResponse(
    @SerializedName("list") val list: List<ForecastItemResponse>,
)

data class ForecastItemResponse(
    @SerializedName("dt") val dt: Long,
    @SerializedName("main") val main: MainResponse,
    @SerializedName("weather") val weather: List<WeatherConditionResponse>,
)
