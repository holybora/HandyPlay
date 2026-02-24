package com.sls.handbook.feature.dp.structural.decorator

import androidx.lifecycle.ViewModel
import com.sls.handbook.core.model.PatternContent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DecoratorViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<DecoratorUiState>(DecoratorUiState.Idle())
    val uiState: StateFlow<DecoratorUiState> = _uiState.asStateFlow()

    val content = PatternContent(
        title = "Decorator",
        subtitle = "Structural Pattern",
        description = "Decorator attaches additional responsibilities to an object dynamically. " +
            "Decorators provide a flexible alternative to subclassing for extending functionality. " +
            "Each decorator wraps the original object and adds behavior before or after delegating " +
            "to the wrapped object.",
        whenToUse = listOf(
            "When you want to add responsibilities to objects dynamically and transparently",
            "When extension by subclassing is impractical due to many independent extensions",
            "When you want to combine behaviors flexibly at runtime",
            "When you need to add cross-cutting concerns like logging, caching, or validation",
        ),
        androidExamples = "OkHttp Interceptors are decorators — each wraps the chain and adds behavior " +
            "(logging, auth headers, caching). InputStream decorators (BufferedInputStream, " +
            "GZIPInputStream) wrap streams to add functionality. Modifier in Jetpack Compose " +
            "chains decorations onto UI elements.",
        structure = """interface Component {
    fun operation(): String
    fun cost(): Double
}

class ConcreteComponent : Component {
    override fun operation() = "Base"
    override fun cost() = 1.0
}

class Decorator(
    private val wrapped: Component
) : Component {
    override fun operation() =
        "Decorated ${'$'}{wrapped.operation()}"
    override fun cost() =
        wrapped.cost() + 0.5
}""",
    )

    fun onToggleMilk(enabled: Boolean) {
        updateState { it.copy(hasMilk = enabled) }
    }

    fun onToggleSugar(enabled: Boolean) {
        updateState { it.copy(hasSugar = enabled) }
    }

    fun onToggleWhippedCream(enabled: Boolean) {
        updateState { it.copy(hasWhippedCream = enabled) }
    }

    fun onToggleCaramel(enabled: Boolean) {
        updateState { it.copy(hasCaramel = enabled) }
    }

    private fun updateState(update: (DecoratorUiState.Idle) -> DecoratorUiState.Idle) {
        val currentState = _uiState.value
        if (currentState !is DecoratorUiState.Idle) return

        val newState = update(currentState)
        var coffee: Coffee = BasicCoffee()

        if (newState.hasMilk) coffee = MilkDecorator(coffee)
        if (newState.hasSugar) coffee = SugarDecorator(coffee)
        if (newState.hasWhippedCream) coffee = WhippedCreamDecorator(coffee)
        if (newState.hasCaramel) coffee = CaramelDecorator(coffee)

        _uiState.value = newState.copy(
            description = coffee.description(),
            price = coffee.cost(),
        )
    }

    private interface Coffee {
        fun description(): String
        fun cost(): Double
    }

    private class BasicCoffee : Coffee {
        override fun description() = "Basic Coffee"
        override fun cost() = BASE_COST

        companion object {
            private const val BASE_COST = 2.00
        }
    }

    private class MilkDecorator(private val coffee: Coffee) : Coffee {
        override fun description() = "Milk ${coffee.description()}"
        override fun cost() = coffee.cost() + MILK_COST

        companion object {
            private const val MILK_COST = 0.50
        }
    }

    private class SugarDecorator(private val coffee: Coffee) : Coffee {
        override fun description() = "Sugar ${coffee.description()}"
        override fun cost() = coffee.cost() + SUGAR_COST

        companion object {
            private const val SUGAR_COST = 0.30
        }
    }

    private class WhippedCreamDecorator(private val coffee: Coffee) : Coffee {
        override fun description() = "Whipped Cream ${coffee.description()}"
        override fun cost() = coffee.cost() + WHIPPED_CREAM_COST

        companion object {
            private const val WHIPPED_CREAM_COST = 0.70
        }
    }

    private class CaramelDecorator(private val coffee: Coffee) : Coffee {
        override fun description() = "Caramel ${coffee.description()}"
        override fun cost() = coffee.cost() + CARAMEL_COST

        companion object {
            private const val CARAMEL_COST = 0.60
        }
    }
}
