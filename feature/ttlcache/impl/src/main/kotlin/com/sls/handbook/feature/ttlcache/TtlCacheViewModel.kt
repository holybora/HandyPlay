package com.sls.handbook.feature.ttlcache

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sls.handbook.core.domain.repository.JokeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class TtlCacheViewModel @Inject constructor(
    private val jokeRepository: JokeRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<TtlCacheUiState>(TtlCacheUiState.Idle())
    val uiState: StateFlow<TtlCacheUiState> = _uiState.asStateFlow()

    fun onTtlChange(ttlSeconds: Int) {
        _uiState.update { currentState ->
            if (currentState is TtlCacheUiState.Idle) {
                currentState.copy(ttlSeconds = ttlSeconds)
            } else {
                currentState
            }
        }
    }

    fun onGetClick() {
        val currentState = _uiState.value
        if (currentState !is TtlCacheUiState.Idle) return

        val ttlSeconds = currentState.ttlSeconds.toLong()

        _uiState.update { (it as? TtlCacheUiState.Idle)?.copy(isLoading = true) ?: it }

        viewModelScope.launch {
            try {
                val result = jokeRepository.getJoke(ttlMillis = ttlSeconds * 1000)
                val timeFormatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                val fetchedTimeStr = timeFormatter.format(Date(result.fetchTimeMillis))
                val sourceLabel = if (result.fromCache) " (from cache)" else " (fresh)"

                _uiState.value = TtlCacheUiState.Idle(
                    ttlSeconds = currentState.ttlSeconds,
                    lastFetchedTime = fetchedTimeStr + sourceLabel,
                    data = "Setup: ${result.joke.setup}\n\nPunchline: ${result.joke.punchline}",
                    isLoading = false,
                )
            } catch (@Suppress("TooGenericExceptionCaught") e: Exception) {
                _uiState.value = TtlCacheUiState.Idle(
                    ttlSeconds = currentState.ttlSeconds,
                    lastFetchedTime = currentState.lastFetchedTime,
                    data = "Error: ${e.message}",
                    isLoading = false,
                )
            }
        }
    }
}
