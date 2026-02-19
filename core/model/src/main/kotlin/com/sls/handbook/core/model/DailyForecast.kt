package com.sls.handbook.core.model

import kotlinx.serialization.Serializable

@Serializable
data class DailyForecast(
    val dateEpochSeconds: Long,
    val tempMin: Double,
    val tempMax: Double,
    val icon: String,
)
