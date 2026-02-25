package com.sls.handbook.feature.dp.creational.abstractfactory

sealed interface AbstractFactoryUiState {
    data class Idle(
        val selectedTheme: ThemeFamily = ThemeFamily.MATERIAL,
        val button: UiComponentDisplay = MaterialUiFactory.createButton(),
        val textField: UiComponentDisplay = MaterialUiFactory.createTextField(),
        val card: UiComponentDisplay = MaterialUiFactory.createCard(),
    ) : AbstractFactoryUiState
}

enum class ThemeFamily(val displayName: String) {
    MATERIAL("Material Design"),
    IOS("iOS Style"),
}

data class UiComponentDisplay(
    val name: String,
    val description: String,
    val colorHex: String,
)

internal interface UiComponentFactory {
    fun createButton(): UiComponentDisplay
    fun createTextField(): UiComponentDisplay
    fun createCard(): UiComponentDisplay
}

internal object MaterialUiFactory : UiComponentFactory {
    override fun createButton() = UiComponentDisplay(
        name = "Button",
        description = "Rounded corners, elevation shadow, ripple effect",
        colorHex = "#6750A4",
    )

    override fun createTextField() = UiComponentDisplay(
        name = "TextField",
        description = "Outlined border, animated floating label",
        colorHex = "#6750A4",
    )

    override fun createCard() = UiComponentDisplay(
        name = "Card",
        description = "Rounded 12dp corners, subtle elevation shadow",
        colorHex = "#FFFBFE",
    )
}

internal object IosUiFactory : UiComponentFactory {
    override fun createButton() = UiComponentDisplay(
        name = "Button",
        description = "Pill shape, no elevation, highlight on press",
        colorHex = "#007AFF",
    )

    override fun createTextField() = UiComponentDisplay(
        name = "TextField",
        description = "Rounded rectangle, inset placeholder text",
        colorHex = "#8E8E93",
    )

    override fun createCard() = UiComponentDisplay(
        name = "Card",
        description = "Grouped inset style, separator lines",
        colorHex = "#F2F2F7",
    )
}
