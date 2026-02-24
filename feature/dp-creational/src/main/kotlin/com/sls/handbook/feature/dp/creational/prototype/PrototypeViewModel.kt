package com.sls.handbook.feature.dp.creational.prototype

import androidx.lifecycle.ViewModel
import com.sls.handbook.core.model.PatternContent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PrototypeViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<PrototypeUiState>(PrototypeUiState.Idle())
    val uiState: StateFlow<PrototypeUiState> = _uiState.asStateFlow()

    val content = PatternContent(
        title = "Prototype",
        subtitle = "Creational Pattern",
        description = "Prototype specifies the kinds of objects to create using a prototypical " +
            "instance, and creates new objects by cloning this prototype. In Kotlin, data classes " +
            "provide a natural implementation through the copy() function, making this pattern " +
            "particularly elegant.",
        whenToUse = listOf(
            "When creating an object is expensive and a similar object already exists",
            "When you want to avoid building a complex class hierarchy of factories",
            "When instances of a class can have only a few different combinations of state",
            "When you need to create objects that are independent copies of existing ones",
        ),
        androidExamples = "Kotlin data classes use copy() as a built-in prototype. Bundle.clone() " +
            "creates copies of bundles. Intent(intent) copies an existing intent. " +
            "SavedStateHandle preserves and restores object state. Configuration copying " +
            "in Android resources.",
        structure = """interface Prototype {
    fun clone(): Prototype
}

// In Kotlin, data classes have
// built-in prototype support:
data class Shape(
    val type: String,
    val color: String,
    val size: Int,
) {
    // copy() is auto-generated
    // clone = original.copy(
    //     color = "Red"
    // )
}""",
    )

    fun onClone() {
        _uiState.update { currentState ->
            if (currentState is PrototypeUiState.Idle) {
                val clone = currentState.originalShape.clone()
                currentState.copy(clones = currentState.clones + clone)
            } else {
                currentState
            }
        }
    }

    fun onSelectClone(index: Int?) {
        _uiState.update { currentState ->
            if (currentState is PrototypeUiState.Idle) {
                currentState.copy(selectedCloneIndex = index)
            } else {
                currentState
            }
        }
    }

    fun onCloneColorChanged(index: Int, colorName: String, colorValue: Long) {
        _uiState.update { currentState ->
            if (currentState is PrototypeUiState.Idle && index in currentState.clones.indices) {
                val updatedClones = currentState.clones.toMutableList()
                updatedClones[index] = updatedClones[index].copy(
                    colorName = colorName,
                    colorValue = colorValue,
                )
                currentState.copy(clones = updatedClones)
            } else {
                currentState
            }
        }
    }

    fun onCloneSizeChanged(index: Int, size: Int) {
        _uiState.update { currentState ->
            if (currentState is PrototypeUiState.Idle && index in currentState.clones.indices) {
                val updatedClones = currentState.clones.toMutableList()
                updatedClones[index] = updatedClones[index].copy(size = size)
                currentState.copy(clones = updatedClones)
            } else {
                currentState
            }
        }
    }

    fun onOriginalTypeChanged(type: ShapeType) {
        _uiState.update { currentState ->
            if (currentState is PrototypeUiState.Idle) {
                currentState.copy(originalShape = currentState.originalShape.copy(type = type))
            } else {
                currentState
            }
        }
    }
}
