package com.sls.handbook.feature.fever

sealed interface FeverUiState {
    data object Loading : FeverUiState
    data class Success(val weatherDisplay: WeatherDisplayData) : FeverUiState
    data class Error(val message: String) : FeverUiState
}
