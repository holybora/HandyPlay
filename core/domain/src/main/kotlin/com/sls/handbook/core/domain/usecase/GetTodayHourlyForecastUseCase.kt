package com.sls.handbook.core.domain.usecase

import com.sls.handbook.core.model.ForecastData
import com.sls.handbook.core.model.HourlyForecast
import javax.inject.Inject

/**
 * Extracts today's hourly forecast entries from raw [ForecastData].
 *
 * Filters forecast items to only those matching today's date in the location's timezone,
 * then maps each [ForecastItem] to an [HourlyForecast].
 */
class GetTodayHourlyForecastUseCase @Inject constructor() {

    operator fun invoke(forecastData: ForecastData): List<HourlyForecast> {
        return forecastData.items
            .map { item ->
                HourlyForecast(
                    dt = item.dt,
                    temperature = item.temperature,
                    icon = item.icon,
                    description = item.description,
                    pop = item.pop,
                )
            }
    }
}
