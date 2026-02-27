package com.sls.handbook.feature.fever

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sls.handbook.core.domain.exception.WeatherException
import com.sls.handbook.core.domain.usecase.GenerateRandomCoordinatesUseCase
import com.sls.handbook.core.domain.usecase.GetCurrentWeatherUseCase
import com.sls.handbook.core.domain.usecase.GetFiveDayForecastUseCase
import com.sls.handbook.core.domain.usecase.GetForecastDataUseCase
import com.sls.handbook.core.domain.usecase.GetTodayHourlyForecastUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Locale
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the Fever weather screen, following the MVI pattern.
 *
 * Orchestrates domain use cases to generate a random location, fetch current weather
 * and forecast data concurrently, then maps results to [WeatherDisplayData] for the UI.
 *
 * @param stringResolver provides localized string resolution without a direct Context dependency
 * @param getCurrentWeather retrieves current weather for a coordinate pair
 * @param getForecastData retrieves raw 3-hourly forecast data
 * @param getFiveDayForecast aggregates forecast data into daily summaries
 * @param getTodayHourlyForecast filters forecast data to today's hourly entries
 * @param generateRandomCoordinates produces a random lat/lon pair
 */
@HiltViewModel
class FeverViewModel @Inject constructor(
    private val stringResolver: StringResolver,
    private val getCurrentWeather: GetCurrentWeatherUseCase,
    private val getForecastData: GetForecastDataUseCase,
    private val getFiveDayForecast: GetFiveDayForecastUseCase,
    private val getTodayHourlyForecast: GetTodayHourlyForecastUseCase,
    private val generateRandomCoordinates: GenerateRandomCoordinatesUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<FeverUiState>(FeverUiState.Loading)
    val uiState: StateFlow<FeverUiState> = _uiState.asStateFlow()

    init {
        loadWeather()
    }

    fun onEvent(event: FeverEvent) {
        when (event) {
            FeverEvent.Refresh -> {
                if (_uiState.value !is FeverUiState.Loading) {
                    loadWeather()
                }
            }
        }
    }

    private fun loadWeather() {
        _uiState.value = FeverUiState.Loading
        viewModelScope.launch {
            try {
                val coordinates = generateRandomCoordinates()
                val lang = Locale.getDefault().language
                val (weather, forecastData) = coroutineScope {
                    val weatherDeferred = async {
                        getCurrentWeather(coordinates.latitude, coordinates.longitude, lang)
                    }
                    val forecastDataDeferred = async {
                        getForecastData(coordinates.latitude, coordinates.longitude, lang)
                    }
                    weatherDeferred.await() to forecastDataDeferred.await()
                }
                val dailyForecast = getFiveDayForecast(forecastData)
                val hourlyForecasts = getTodayHourlyForecast(forecastData)
                // Trade-off: fake delay to make animation transition smooth, could be removed if desired
                delay(FadeDurationMs.toLong())
                _uiState.value = FeverUiState.Success(
                    weather.toDisplayData(
                        stringResolver = stringResolver,
                        dailyForecast = dailyForecast,
                        hourlyForecasts = hourlyForecasts,
                    ),
                )
            } catch (e: CancellationException) {
                throw e
            } catch (_: WeatherException.Network) {
                _uiState.value = FeverUiState.Error(
                    stringResolver.getString(R.string.fever_network_error),
                )
            } catch (_: WeatherException.Server) {
                _uiState.value = FeverUiState.Error(
                    stringResolver.getString(R.string.fever_server_error),
                )
            } catch (_: WeatherException.DataParsing) {
                _uiState.value = FeverUiState.Error(
                    stringResolver.getString(R.string.fever_unknown_error),
                )
            } catch (@Suppress("TooGenericExceptionCaught") _: Exception) {
                _uiState.value = FeverUiState.Error(
                    stringResolver.getString(R.string.fever_unknown_error),
                )
            }
        }
    }
}
