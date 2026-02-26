package com.sls.handbook.core.domain.usecase

import com.sls.handbook.core.domain.repository.WeatherRepository
import com.sls.handbook.core.model.Weather
import javax.inject.Inject

/**
 * Retrieves current weather conditions for a given location.
 *
 * Delegates to [WeatherRepository.getWeather].
 *
 * @param weatherRepository data source for weather information
 */
class GetCurrentWeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository,
) {
    suspend operator fun invoke(lat: Double, lon: Double, lang: String): Weather =
        weatherRepository.getWeather(lat, lon, lang)
}
