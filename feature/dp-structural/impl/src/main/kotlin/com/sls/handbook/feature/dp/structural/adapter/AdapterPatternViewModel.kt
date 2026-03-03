package com.sls.handbook.feature.dp.structural.adapter

import androidx.lifecycle.ViewModel
import com.sls.handbook.core.model.PatternContent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AdapterPatternViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<AdapterPatternUiState>(AdapterPatternUiState.Idle())
    val uiState: StateFlow<AdapterPatternUiState> = _uiState.asStateFlow()

    val content = PatternContent(
        title = "Adapter",
        subtitle = "Structural Pattern",
        description = "Adapter converts the interface of a class into another interface clients expect. " +
            "It lets classes work together that couldn't otherwise because of incompatible interfaces. " +
            "The adapter wraps an existing class with a new interface so it can be used where needed.",
        whenToUse = listOf(
            "When you want to use an existing class but its interface doesn't match what you need",
            "When you want to create a reusable class that cooperates with unrelated classes",
            "When you need to use several existing subclasses without adapting each one",
            "When integrating third-party libraries with different data formats",
        ),
        androidExamples = "RecyclerView.Adapter converts data models to ViewHolders. ListAdapter adapts " +
            "DiffUtil for efficient list updates. Retrofit converters adapt HTTP responses to Kotlin " +
            "objects. TypeConverter in Room adapts custom types to storable formats.",
        structure = """interface Target {
    fun request(): String
}

class Adaptee {
    fun specificRequest(): String =
        "Adaptee data"
}

class Adapter(
    private val adaptee: Adaptee
) : Target {
    override fun request(): String =
        adaptee.specificRequest()
            .transformed()
}""",
    )

    fun onSystemToggle(useMetric: Boolean) {
        val currentState = _uiState.value
        if (currentState !is AdapterPatternUiState.Idle) return

        val adapter: WeatherAdapter = if (useMetric) MetricAdapter else ImperialAdapter
        val adapted = adapter.adapt(currentState.rawData)

        _uiState.value = currentState.copy(
            useMetric = useMetric,
            adaptedData = adapted,
        )
    }

    fun onTemperatureChanged(fahrenheit: Double) {
        updateRawData { it.copy(temperatureFahrenheit = fahrenheit) }
    }

    fun onDistanceChanged(miles: Double) {
        updateRawData { it.copy(distanceMiles = miles) }
    }

    private fun updateRawData(update: (WeatherDataRaw) -> WeatherDataRaw) {
        val currentState = _uiState.value
        if (currentState !is AdapterPatternUiState.Idle) return

        val newRaw = update(currentState.rawData)
        val adapter: WeatherAdapter = if (currentState.useMetric) MetricAdapter else ImperialAdapter

        _uiState.value = currentState.copy(
            rawData = newRaw,
            adaptedData = adapter.adapt(newRaw),
        )
    }
}
