package com.sls.handbook.feature.home

import com.sls.handbook.core.model.Category

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(
        val categories: List<Category>,
        val searchQuery: String = "",
    ) : HomeUiState
    data class Error(val message: String) : HomeUiState
}
