package com.sls.handbook.feature.dp.behavioral.command

import androidx.lifecycle.ViewModel
import com.sls.handbook.core.model.PatternContent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CommandViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<CommandUiState>(CommandUiState.Idle())
    val uiState: StateFlow<CommandUiState> = _uiState.asStateFlow()

    val content = PatternContent(
        title = "Command",
        subtitle = "Behavioral Pattern",
        description = "Command encapsulates a request as an object, thereby letting you parameterize " +
            "clients with different requests, queue or log requests, and support undoable " +
            "operations. It decouples the object that invokes the operation from the one that " +
            "knows how to perform it.",
        whenToUse = listOf(
            "When you want to parameterize objects with operations",
            "When you need to queue, schedule, or execute requests at different times",
            "When you need reversible (undo/redo) operations",
            "When you want to structure a system around high-level operations built on primitives",
        ),
        androidExamples = "Android's Handler.post(Runnable) treats runnables as commands. " +
            "Navigation actions are command objects that encapsulate navigation requests. " +
            "Room database transactions can be modeled as commands. " +
            "WorkManager OneTimeWorkRequest encapsulates deferred work as command objects.",
        structure = """interface Command {
    fun execute()
    fun undo()
}

class ConcreteCommand(
    private val receiver: Receiver
) : Command {
    override fun execute() {
        receiver.action()
    }
    override fun undo() {
        receiver.reverseAction()
    }
}

class Invoker {
    private val history =
        mutableListOf<Command>()

    fun executeCommand(cmd: Command) {
        cmd.execute()
        history.add(cmd)
    }

    fun undoLast() {
        history.removeLastOrNull()?.undo()
    }
}""",
    )

    fun onToggleBold() {
        val currentState = _uiState.value
        if (currentState !is CommandUiState.Idle) return

        val command = TextCommand.ToggleBold(currentState.isBold)
        _uiState.value = currentState.copy(
            isBold = !currentState.isBold,
            undoStack = currentState.undoStack + command,
            redoStack = emptyList(),
            commandHistory = currentState.commandHistory + "Execute: ${command.description}",
        )
    }

    fun onToggleItalic() {
        val currentState = _uiState.value
        if (currentState !is CommandUiState.Idle) return

        val command = TextCommand.ToggleItalic(currentState.isItalic)
        _uiState.value = currentState.copy(
            isItalic = !currentState.isItalic,
            undoStack = currentState.undoStack + command,
            redoStack = emptyList(),
            commandHistory = currentState.commandHistory + "Execute: ${command.description}",
        )
    }

    fun onToggleUnderline() {
        val currentState = _uiState.value
        if (currentState !is CommandUiState.Idle) return

        val command = TextCommand.ToggleUnderline(currentState.isUnderline)
        _uiState.value = currentState.copy(
            isUnderline = !currentState.isUnderline,
            undoStack = currentState.undoStack + command,
            redoStack = emptyList(),
            commandHistory = currentState.commandHistory + "Execute: ${command.description}",
        )
    }

    fun onTextChanged(newText: String) {
        val currentState = _uiState.value
        if (currentState !is CommandUiState.Idle) return

        val command = TextCommand.ChangeText(currentState.text, newText)
        _uiState.value = currentState.copy(
            text = newText,
            undoStack = currentState.undoStack + command,
            redoStack = emptyList(),
            commandHistory = currentState.commandHistory + "Execute: ${command.description}",
        )
    }

    fun onUndo() {
        val currentState = _uiState.value
        if (currentState !is CommandUiState.Idle) return
        if (currentState.undoStack.isEmpty()) return

        val command = currentState.undoStack.last()
        val newUndoStack = currentState.undoStack.dropLast(1)

        val newState = when (command) {
            is TextCommand.ToggleBold -> currentState.copy(isBold = command.previous)
            is TextCommand.ToggleItalic -> currentState.copy(isItalic = command.previous)
            is TextCommand.ToggleUnderline -> currentState.copy(isUnderline = command.previous)
            is TextCommand.ChangeText -> currentState.copy(text = command.previousText)
        }

        _uiState.value = newState.copy(
            undoStack = newUndoStack,
            redoStack = currentState.redoStack + command,
            commandHistory = currentState.commandHistory + "Undo: ${command.description}",
        )
    }

    fun onRedo() {
        val currentState = _uiState.value
        if (currentState !is CommandUiState.Idle) return
        if (currentState.redoStack.isEmpty()) return

        val command = currentState.redoStack.last()
        val newRedoStack = currentState.redoStack.dropLast(1)

        val newState = when (command) {
            is TextCommand.ToggleBold -> currentState.copy(isBold = !command.previous)
            is TextCommand.ToggleItalic -> currentState.copy(isItalic = !command.previous)
            is TextCommand.ToggleUnderline -> currentState.copy(isUnderline = !command.previous)
            is TextCommand.ChangeText -> currentState.copy(text = command.newText)
        }

        _uiState.value = newState.copy(
            undoStack = currentState.undoStack + command,
            redoStack = newRedoStack,
            commandHistory = currentState.commandHistory + "Redo: ${command.description}",
        )
    }
}
