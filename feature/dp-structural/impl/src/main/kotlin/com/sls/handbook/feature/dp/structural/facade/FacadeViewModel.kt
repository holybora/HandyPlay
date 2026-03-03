package com.sls.handbook.feature.dp.structural.facade

import androidx.lifecycle.ViewModel
import com.sls.handbook.core.model.PatternContent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class FacadeViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<FacadeUiState>(FacadeUiState.Idle())
    val uiState: StateFlow<FacadeUiState> = _uiState.asStateFlow()

    val content = PatternContent(
        title = "Facade",
        subtitle = "Structural Pattern",
        description = "Facade provides a unified interface to a set of interfaces in a subsystem. " +
            "It defines a higher-level interface that makes the subsystem easier to use. " +
            "Instead of interacting with multiple complex subsystems directly, clients " +
            "use the facade's simplified interface.",
        whenToUse = listOf(
            "When you want to provide a simple interface to a complex subsystem",
            "When there are many dependencies between clients and implementation classes",
            "When you want to layer your subsystems and define entry points to each level",
            "When you need to decouple a subsystem from its clients",
        ),
        androidExamples = "MediaPlayer is a facade over audio decoding, rendering, and output subsystems. " +
            "Retrofit is a facade over OkHttp, converters, and call adapters. Room provides a " +
            "facade over SQLite operations. WorkManager facades job scheduling, constraints, " +
            "and execution.",
        structure = """class Facade(
    private val subsystemA: SubsystemA,
    private val subsystemB: SubsystemB,
    private val subsystemC: SubsystemC,
) {
    fun simpleOperation() {
        subsystemA.operationA()
        subsystemB.operationB()
        subsystemC.operationC()
    }
}

// Client uses only:
// facade.simpleOperation()
// instead of calling A, B, C directly""",
    )

    fun onToggleFacade(useFacade: Boolean) {
        _uiState.update { currentState ->
            if (currentState is FacadeUiState.Idle) {
                currentState.copy(
                    useFacade = useFacade,
                    projectorOn = false,
                    soundSurround = false,
                    lightsDimmed = false,
                    log = emptyList(),
                )
            } else {
                currentState
            }
        }
    }

    fun onWatchMovie() {
        _uiState.update { currentState ->
            if (currentState is FacadeUiState.Idle) {
                currentState.copy(
                    projectorOn = true,
                    soundSurround = true,
                    lightsDimmed = true,
                    log = currentState.log + listOf(
                        "Facade: watchMovie() called",
                        "  → Projector.turnOn()",
                        "  → SoundSystem.setSurround()",
                        "  → Lights.dim()",
                    ),
                )
            } else {
                currentState
            }
        }
    }

    fun onStopMovie() {
        _uiState.update { currentState ->
            if (currentState is FacadeUiState.Idle) {
                currentState.copy(
                    projectorOn = false,
                    soundSurround = false,
                    lightsDimmed = false,
                    log = currentState.log + listOf(
                        "Facade: stopMovie() called",
                        "  → Lights.brighten()",
                        "  → SoundSystem.off()",
                        "  → Projector.turnOff()",
                    ),
                )
            } else {
                currentState
            }
        }
    }

    fun onToggleProjector(on: Boolean) {
        _uiState.update { currentState ->
            if (currentState is FacadeUiState.Idle) {
                val action = if (on) "Projector.turnOn()" else "Projector.turnOff()"
                currentState.copy(
                    projectorOn = on,
                    log = currentState.log + action,
                )
            } else {
                currentState
            }
        }
    }

    fun onToggleSound(surround: Boolean) {
        _uiState.update { currentState ->
            if (currentState is FacadeUiState.Idle) {
                val action = if (surround) "SoundSystem.setSurround()" else "SoundSystem.off()"
                currentState.copy(
                    soundSurround = surround,
                    log = currentState.log + action,
                )
            } else {
                currentState
            }
        }
    }

    fun onToggleLights(dimmed: Boolean) {
        _uiState.update { currentState ->
            if (currentState is FacadeUiState.Idle) {
                val action = if (dimmed) "Lights.dim()" else "Lights.brighten()"
                currentState.copy(
                    lightsDimmed = dimmed,
                    log = currentState.log + action,
                )
            } else {
                currentState
            }
        }
    }
}
