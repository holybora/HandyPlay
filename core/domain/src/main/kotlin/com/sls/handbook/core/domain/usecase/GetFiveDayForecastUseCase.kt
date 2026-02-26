package com.sls.handbook.core.domain.usecase

import com.sls.handbook.core.model.DailyForecast
import com.sls.handbook.core.model.ForecastData
import com.sls.handbook.core.model.ForecastItem
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import javax.inject.Inject
import kotlin.math.abs

/**
 * Transforms raw [ForecastData] into a list of [DailyForecast] entries for the next five days.
 *
 * Groups forecast items by UTC date, filters out today's entries, then aggregates each
 * remaining day by selecting the midday icon and computing the overall min/max temperatures.
 */
class GetFiveDayForecastUseCase @Inject constructor() {

    operator fun invoke(forecastData: ForecastData): List<DailyForecast> {
        val today = LocalDate.now(ZoneOffset.UTC)
        return forecastData.items
            .groupBy { item ->
                Instant.ofEpochSecond(item.dt).atZone(ZoneOffset.UTC).toLocalDate()
            }
            .filterKeys { it.isAfter(today) }
            .toSortedMap()
            .map { (_, items) -> aggregateDay(items) }
    }

    private fun aggregateDay(items: List<ForecastItem>): DailyForecast {
        val middayItem = items.minBy { item ->
            val hour = Instant.ofEpochSecond(item.dt).atZone(ZoneOffset.UTC).hour
            abs(hour - MIDDAY_HOUR)
        }
        return DailyForecast(
            dateEpochSeconds = middayItem.dt,
            tempMin = items.minOf { it.tempMin },
            tempMax = items.maxOf { it.tempMax },
            icon = middayItem.icon,
        )
    }

    private companion object {
        const val MIDDAY_HOUR = 12
    }
}
