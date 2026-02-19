package com.sls.handbook.core.model

import kotlinx.serialization.Serializable

@Serializable
data class HourlyForecast(
    val dt: Long,
    val temperature: Double,
    val icon: String,
    val description: String,
    val pop: Double,
)
