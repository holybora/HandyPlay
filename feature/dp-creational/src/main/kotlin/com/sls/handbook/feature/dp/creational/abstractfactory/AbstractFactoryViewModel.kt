package com.sls.handbook.feature.dp.creational.abstractfactory

import androidx.lifecycle.ViewModel
import com.sls.handbook.core.model.PatternContent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AbstractFactoryViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<AbstractFactoryUiState>(AbstractFactoryUiState.Idle())
    val uiState: StateFlow<AbstractFactoryUiState> = _uiState.asStateFlow()

    val content = PatternContent(
        title = "Abstract Factory",
        subtitle = "Creational Pattern",
        description = "Abstract Factory provides an interface for creating families of related or " +
            "dependent objects without specifying their concrete classes. Unlike Factory Method " +
            "which creates one product, Abstract Factory creates entire families of products " +
            "that are designed to work together.",
        whenToUse = listOf(
            "When a system should be independent of how its products are created",
            "When a system should be configured with one of multiple families of products",
            "When a family of related products must be used together",
            "When you want to provide a library of products revealing only interfaces",
        ),
        androidExamples = "MaterialTheme produces coordinated colors, typography, and shapes — " +
            "an abstract factory of design tokens. Similarly, AppCompat vs Material component " +
            "factories produce different styled widgets that work together. Platform-specific " +
            "UI adapters also follow this pattern.",
        structure = """interface AbstractFactory {
    fun createProductA(): ProductA
    fun createProductB(): ProductB
}

class ConcreteFactory1 : AbstractFactory {
    override fun createProductA() = ProductA1()
    override fun createProductB() = ProductB1()
}

class ConcreteFactory2 : AbstractFactory {
    override fun createProductA() = ProductA2()
    override fun createProductB() = ProductB2()
}""",
    )

    fun onThemeSelected(theme: ThemeFamily) {
        val factory: UiComponentFactory = when (theme) {
            ThemeFamily.MATERIAL -> MaterialUiFactory
            ThemeFamily.IOS -> IosUiFactory
        }

        _uiState.value = AbstractFactoryUiState.Idle(
            selectedTheme = theme,
            button = factory.createButton(),
            textField = factory.createTextField(),
            card = factory.createCard(),
        )
    }
}
