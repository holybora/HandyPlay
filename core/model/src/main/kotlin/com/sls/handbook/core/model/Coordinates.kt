package com.sls.handbook.core.model

/**
 * Geographic coordinate pair representing a location on Earth.
 *
 * @property latitude decimal degrees north (negative for south), range -90 to 90
 * @property longitude decimal degrees east (negative for west), range -180 to 180
 */
data class Coordinates(
    val latitude: Double,
    val longitude: Double,
)
