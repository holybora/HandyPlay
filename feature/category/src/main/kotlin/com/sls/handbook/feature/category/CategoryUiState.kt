package com.sls.handbook.feature.category

import com.sls.handbook.core.model.Topic

sealed interface CategoryUiState {
    data object Loading : CategoryUiState
    data class Success(
        val categoryName: String,
        val topics: List<Topic>,
        val searchQuery: String = "",
    ) : CategoryUiState
    data class Error(val message: String) : CategoryUiState
}
