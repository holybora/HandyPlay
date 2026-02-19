package com.sls.handbook.core.network.model

import com.google.gson.annotations.SerializedName

data class HourlyForecastResponse(
    @SerializedName("list") val list: List<HourlyEntryResponse>,
    @SerializedName("city") val city: CityResponse,
)

data class HourlyEntryResponse(
    @SerializedName("dt") val dt: Long,
    @SerializedName("main") val main: MainResponse,
    @SerializedName("weather") val weather: List<WeatherConditionResponse>,
    @SerializedName("pop") val pop: Double,
)

data class CityResponse(
    @SerializedName("timezone") val timezone: Int,
)
