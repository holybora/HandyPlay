package com.sls.handbook.feature.fever

import com.sls.handbook.core.model.Weather

sealed interface FeverUiState {
    data object Loading : FeverUiState
    data class Success(val weather: Weather) : FeverUiState
    data class Error(val message: String) : FeverUiState
}
