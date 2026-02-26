package com.sls.handbook.core.model

import kotlinx.serialization.Serializable

/**
 * Container for raw 3-hourly forecast entries and the location's timezone offset.
 *
 * @property items ordered list of [ForecastItem] entries from the API
 * @property timezoneOffsetSeconds UTC offset of the forecast location, in seconds
 */
@Serializable
data class ForecastData(
    val items: List<ForecastItem>,
    val timezoneOffsetSeconds: Int,
)
