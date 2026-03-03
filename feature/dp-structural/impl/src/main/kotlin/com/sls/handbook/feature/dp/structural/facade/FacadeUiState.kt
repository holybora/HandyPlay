package com.sls.handbook.feature.dp.structural.facade

sealed interface FacadeUiState {
    data class Idle(
        val useFacade: Boolean = true,
        val projectorOn: Boolean = false,
        val soundSurround: Boolean = false,
        val lightsDimmed: Boolean = false,
        val log: List<String> = emptyList(),
    ) : FacadeUiState
}
