package com.sls.handbook.feature.dp.behavioral.observer

import androidx.lifecycle.ViewModel
import com.sls.handbook.core.model.PatternContent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ObserverViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<ObserverUiState>(ObserverUiState.Idle())
    val uiState: StateFlow<ObserverUiState> = _uiState.asStateFlow()

    private var previousPrice = 100.0

    companion object {
        private const val RAPID_THRESHOLD = 5.0
        private const val TREND_THRESHOLD = 2.0
    }

    val content = PatternContent(
        title = "Observer",
        subtitle = "Behavioral Pattern",
        description = "Observer defines a one-to-many dependency between objects so that when one " +
            "object changes state, all its dependents are notified and updated automatically. " +
            "This is the foundation of reactive programming and event-driven architectures.",
        whenToUse = listOf(
            "When a change to one object requires changing others, and you don't know how many",
            "When an object should notify other objects without knowing who they are",
            "When you need to maintain consistency between related objects loosely",
            "When building event systems, data binding, or reactive streams",
        ),
        androidExamples = "LiveData and StateFlow are observer implementations — observers subscribe to " +
            "state changes. Flow.collect() is an observer pattern. BroadcastReceiver observes " +
            "system events. Lifecycle observers watch Activity/Fragment lifecycle changes. " +
            "Data Binding observes model changes to update UI.",
        structure = """interface Observer {
    fun update(value: Any)
}

class Subject {
    private val observers =
        mutableListOf<Observer>()

    fun subscribe(o: Observer) {
        observers.add(o)
    }

    fun unsubscribe(o: Observer) {
        observers.remove(o)
    }

    fun notifyObservers(value: Any) {
        observers.forEach { it.update(value) }
    }
}""",
    )

    fun onPriceChanged(price: Double) {
        val currentState = _uiState.value
        if (currentState !is ObserverUiState.Idle) return

        val portfolioValue = if (currentState.portfolioSubscribed) {
            "${'$'}%.2f".format(price)
        } else {
            currentState.portfolioValue
        }

        val alertMessage = if (currentState.alertSubscribed) {
            when {
                price > previousPrice + RAPID_THRESHOLD -> "Price RISING rapidly!"
                price < previousPrice - RAPID_THRESHOLD -> "Price FALLING rapidly!"
                price > previousPrice -> "Price trending up"
                price < previousPrice -> "Price trending down"
                else -> "Price is stable"
            }
        } else {
            currentState.alertMessage
        }

        val chartTrend = if (currentState.chartSubscribed) {
            when {
                price > previousPrice + TREND_THRESHOLD -> "↑ Bullish"
                price < previousPrice - TREND_THRESHOLD -> "↓ Bearish"
                else -> "→ Flat"
            }
        } else {
            currentState.chartTrend
        }

        previousPrice = price
        _uiState.value = currentState.copy(
            stockPrice = price,
            portfolioValue = portfolioValue,
            alertMessage = alertMessage,
            chartTrend = chartTrend,
        )
    }

    fun onTogglePortfolio(subscribed: Boolean) {
        val currentState = _uiState.value
        if (currentState !is ObserverUiState.Idle) return
        _uiState.value = currentState.copy(
            portfolioSubscribed = subscribed,
            portfolioValue = if (!subscribed) {
                currentState.portfolioValue
            } else {
                "${'$'}%.2f".format(currentState.stockPrice)
            },
        )
    }

    fun onToggleAlert(subscribed: Boolean) {
        val currentState = _uiState.value
        if (currentState !is ObserverUiState.Idle) return
        _uiState.value = currentState.copy(
            alertSubscribed = subscribed,
            alertMessage = if (!subscribed) currentState.alertMessage else "Resubscribed",
        )
    }

    fun onToggleChart(subscribed: Boolean) {
        val currentState = _uiState.value
        if (currentState !is ObserverUiState.Idle) return
        _uiState.value = currentState.copy(
            chartSubscribed = subscribed,
            chartTrend = if (!subscribed) currentState.chartTrend else "→ Flat",
        )
    }
}
