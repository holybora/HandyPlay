package com.sls.handbook.feature.fever

import javax.inject.Inject
import kotlin.random.Random

data class Coordinates(val latitude: Double, val longitude: Double)

class RandomCoordinatesGenerator @Inject constructor() {

    fun generate(): Coordinates {
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
