package com.sls.handbook.feature.fever

sealed class FeverUiState(open val weatherDisplay: WeatherDisplayData) {
    object Loading : FeverUiState(weatherDisplay = WeatherDisplayData.empty())
    data class Success(override val weatherDisplay: WeatherDisplayData) : FeverUiState(weatherDisplay = weatherDisplay)
    data class Error(val message: String) : FeverUiState(weatherDisplay = WeatherDisplayData.empty())
}
