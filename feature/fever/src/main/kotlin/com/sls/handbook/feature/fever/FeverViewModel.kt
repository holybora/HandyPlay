package com.sls.handbook.feature.fever

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sls.handbook.core.domain.repository.WeatherRepository
import com.sls.handbook.feature.fever.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class FeverViewModel @Inject constructor(
    private val stringResolver: StringResolver,
    private val weatherRepository: WeatherRepository,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher,
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
        viewModelScope.launch(ioDispatcher) {
            try {
                val (weather, forecast) = weatherRepository.getWeatherWithForecast()
                // fake delay to make animation transition smooth
                delay(FadeDurationMs.toLong())
                _uiState.value = FeverUiState.Success(
                    weather.toDisplayData(stringResolver, forecast),
                )
            } catch (@Suppress("TooGenericExceptionCaught") e: Exception) {
                _uiState.value = FeverUiState.Error(
                    e.message ?: stringResolver.getString(R.string.fever_unknown_error),
                )
            }
        }
    }
}
