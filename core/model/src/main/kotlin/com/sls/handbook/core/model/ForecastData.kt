package com.sls.handbook.core.model

/**
 * Container for raw 3-hourly forecast entries and the location's timezone offset.
 *
 * @property items ordered list of [ForecastItem] entries from the API
 * @property timezoneOffsetSeconds UTC offset of the forecast location, in seconds
 */
data class ForecastData(
    val items: List<ForecastItem>,
    val timezoneOffsetSeconds: Int,
)
