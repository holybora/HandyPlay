package com.sls.handbook.feature.topic

import com.sls.handbook.core.model.TopicContent

sealed interface TopicUiState {
    data object Loading : TopicUiState
    data class Success(
        val topicName: String,
        val categoryName: String,
        val content: TopicContent,
    ) : TopicUiState
    data class Error(val message: String) : TopicUiState
}
