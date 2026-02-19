package com.sls.handbook.feature.fever

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sls.handbook.core.domain.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeverViewModel @Inject constructor(
    private val stringResolver: StringResolver,
    private val weatherRepository: WeatherRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<FeverUiState>(FeverUiState.Loading)
    val uiState: StateFlow<FeverUiState> = _uiState.asStateFlow()

    init {
        loadWeather()
    }

    fun refresh() {
        loadWeather()
    }

    private fun loadWeather() {
        _uiState.value = FeverUiState.Loading
        viewModelScope.launch {
            try {
                val weather = weatherRepository.getWeatherForRandomLocation()
                _uiState.value = FeverUiState.Success(weather.toDisplayData(stringResolver))
            } catch (@Suppress("TooGenericExceptionCaught") e: Exception) {
                _uiState.value = FeverUiState.Error(
                    e.message ?: stringResolver.getString(R.string.fever_unknown_error),
                )
            }
        }
    }
}
