package com.sls.handbook.feature.dp.structural.facade

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun FacadeRoute(
    modifier: Modifier = Modifier,
    viewModel: FacadeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    FacadeScreen(
        uiState = uiState,
        content = viewModel.content,
        onToggleFacade = viewModel::onToggleFacade,
        onWatchMovie = viewModel::onWatchMovie,
        onStopMovie = viewModel::onStopMovie,
        onToggleProjector = viewModel::onToggleProjector,
        onToggleSound = viewModel::onToggleSound,
        onToggleLights = viewModel::onToggleLights,
        modifier = modifier,
    )
}
