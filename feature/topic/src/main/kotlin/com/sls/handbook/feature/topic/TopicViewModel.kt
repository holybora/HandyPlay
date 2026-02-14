package com.sls.handbook.feature.topic

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.sls.handbook.navigation.TopicDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class TopicViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val destination = savedStateHandle.toRoute<TopicDestination>()

    private val _uiState = MutableStateFlow<TopicUiState>(loadContent())
    val uiState: StateFlow<TopicUiState> = _uiState.asStateFlow()

    private fun loadContent(): TopicUiState {
        val content = TopicContentProvider.getContent(
            topicId = destination.topicId,
            topicName = destination.topicName,
        )
        return if (content != null) {
            TopicUiState.Success(
                topicName = destination.topicName,
                categoryName = destination.categoryName,
                content = content,
            )
        } else {
            TopicUiState.Error("Content not found for topic: ${destination.topicName}")
        }
    }
}
