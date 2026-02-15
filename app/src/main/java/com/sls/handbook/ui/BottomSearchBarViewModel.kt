package com.sls.handbook.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BottomSearchBarViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(BottomSearchBarUiState())
    val uiState: StateFlow<BottomSearchBarUiState> = _uiState.asStateFlow()

    private val _navigationEvents = Channel<BottomSearchBarEvent>(Channel.BUFFERED)
    val navigationEvents = _navigationEvents.receiveAsFlow()

    fun onDestinationChanged(screen: CurrentScreen) {
        _uiState.update {
            BottomSearchBarUiState(
                isVisible = screen is CurrentScreen.Home || screen is CurrentScreen.Category,
                pathSegments = when (screen) {
                    is CurrentScreen.Home -> listOf("Home")
                    is CurrentScreen.Category -> listOf("Home", screen.categoryName)
                    CurrentScreen.Other -> emptyList()
                },
                searchQuery = "",
            )
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun onSegmentClick(index: Int) {
        viewModelScope.launch {
            when (index) {
                0 -> _navigationEvents.send(BottomSearchBarEvent.NavigateToHome)
            }
        }
    }
}

data class BottomSearchBarUiState(
    val isVisible: Boolean = false,
    val pathSegments: List<String> = emptyList(),
    val searchQuery: String = "",
)

sealed interface BottomSearchBarEvent {
    data object NavigateToHome : BottomSearchBarEvent
}

sealed interface CurrentScreen {
    data object Home : CurrentScreen
    data class Category(val categoryName: String) : CurrentScreen
    data object Other : CurrentScreen
}
