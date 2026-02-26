package com.sls.handbook.core.domain.usecase

import com.sls.handbook.core.model.Coordinates
import javax.inject.Inject
import kotlin.random.Random

/**
 * Generates a random geographic [Coordinates] pair within valid Earth bounds.
 *
 * Latitude is sampled uniformly from [-90, 90) and longitude from [-180, 180).
 */
class GenerateRandomCoordinatesUseCase @Inject constructor() {

    operator fun invoke(): Coordinates {
        val lat = Random.nextDouble(LAT_MIN, LAT_MAX)
        val lon = Random.nextDouble(LON_MIN, LON_MAX)
        return Coordinates(lat, lon)
    }

    internal companion object {
        const val LAT_MIN = -90.0
        const val LAT_MAX = 90.0
        const val LON_MIN = -180.0
        const val LON_MAX = 180.0
    }
}
