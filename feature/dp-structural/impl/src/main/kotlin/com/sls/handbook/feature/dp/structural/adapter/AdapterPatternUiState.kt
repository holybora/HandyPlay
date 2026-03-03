package com.sls.handbook.feature.dp.structural.adapter

sealed interface AdapterPatternUiState {
    data class Idle(
        val useMetric: Boolean = true,
        val rawData: WeatherDataRaw = DEFAULT_RAW_DATA,
        val adaptedData: WeatherDataAdapted = MetricAdapter.adapt(DEFAULT_RAW_DATA),
    ) : AdapterPatternUiState

    companion object {
        private val DEFAULT_RAW_DATA = WeatherDataRaw(
            temperatureFahrenheit = DEFAULT_TEMPERATURE_F,
            distanceMiles = DEFAULT_DISTANCE_MI,
            speedMph = DEFAULT_SPEED_MPH,
            pressureInHg = DEFAULT_PRESSURE_INHG,
        )

        private const val DEFAULT_TEMPERATURE_F = 72.0
        private const val DEFAULT_DISTANCE_MI = 10.0
        private const val DEFAULT_SPEED_MPH = 15.0
        private const val DEFAULT_PRESSURE_INHG = 29.92
    }
}

data class WeatherDataRaw(
    val temperatureFahrenheit: Double,
    val distanceMiles: Double,
    val speedMph: Double,
    val pressureInHg: Double,
)

data class WeatherDataAdapted(
    val temperature: String,
    val distance: String,
    val speed: String,
    val pressure: String,
)

internal interface WeatherAdapter {
    fun adapt(raw: WeatherDataRaw): WeatherDataAdapted
}

internal object MetricAdapter : WeatherAdapter {
    private const val FAHRENHEIT_OFFSET = 32
    private const val FAHRENHEIT_TO_CELSIUS_NUMERATOR = 5
    private const val FAHRENHEIT_TO_CELSIUS_DENOMINATOR = 9
    private const val MILES_TO_KM = 1.60934
    private const val INHG_TO_HPA = 33.8639

    override fun adapt(raw: WeatherDataRaw) = WeatherDataAdapted(
        temperature = "%.1f °C".format(
            (raw.temperatureFahrenheit - FAHRENHEIT_OFFSET) *
                FAHRENHEIT_TO_CELSIUS_NUMERATOR / FAHRENHEIT_TO_CELSIUS_DENOMINATOR,
        ),
        distance = "%.1f km".format(raw.distanceMiles * MILES_TO_KM),
        speed = "%.1f km/h".format(raw.speedMph * MILES_TO_KM),
        pressure = "%.0f hPa".format(raw.pressureInHg * INHG_TO_HPA),
    )
}

internal object ImperialAdapter : WeatherAdapter {
    override fun adapt(raw: WeatherDataRaw) = WeatherDataAdapted(
        temperature = "%.1f °F".format(raw.temperatureFahrenheit),
        distance = "%.1f mi".format(raw.distanceMiles),
        speed = "%.1f mph".format(raw.speedMph),
        pressure = "%.2f inHg".format(raw.pressureInHg),
    )
}
