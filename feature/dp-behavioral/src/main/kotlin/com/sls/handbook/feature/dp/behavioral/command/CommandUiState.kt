package com.sls.handbook.feature.dp.behavioral.command

sealed interface CommandUiState {
    data class Idle(
        val text: String = "Hello, World!",
        val isBold: Boolean = false,
        val isItalic: Boolean = false,
        val isUnderline: Boolean = false,
        val commandHistory: List<String> = emptyList(),
        val undoStack: List<TextCommand> = emptyList(),
        val redoStack: List<TextCommand> = emptyList(),
    ) : CommandUiState
}

sealed interface TextCommand {
    val description: String

    data class ToggleBold(val previous: Boolean) : TextCommand {
        override val description: String = if (previous) "Unbold" else "Bold"
    }

    data class ToggleItalic(val previous: Boolean) : TextCommand {
        override val description: String = if (previous) "Unitalic" else "Italic"
    }

    data class ToggleUnderline(val previous: Boolean) : TextCommand {
        override val description: String = if (previous) "Remove Underline" else "Underline"
    }

    data class ChangeText(val previousText: String, val newText: String) : TextCommand {
        override val description: String = "Change Text"
    }
}
