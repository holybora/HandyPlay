package com.sls.handbook.feature.dp.creational.prototype

sealed interface PrototypeUiState {
    data class Idle(
        val originalShape: ShapePrototype = ShapePrototype(
            type = ShapeType.CIRCLE,
            colorName = "Blue",
            colorValue = 0xFF2196F3,
            size = 100,
        ),
        val clones: List<ShapePrototype> = emptyList(),
        val selectedCloneIndex: Int? = null,
    ) : PrototypeUiState
}

enum class ShapeType(val displayName: String) {
    CIRCLE("Circle"),
    SQUARE("Square"),
    TRIANGLE("Triangle"),
}

data class ShapePrototype(
    val type: ShapeType,
    val colorName: String,
    val colorValue: Long,
    val size: Int,
) {
    fun clone(): ShapePrototype = copy()
}
