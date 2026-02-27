package com.sls.handbook.core.model

data class DailyForecast(
    val dateEpochSeconds: Long,
    val tempMin: Double,
    val tempMax: Double,
    val icon: String,
)
