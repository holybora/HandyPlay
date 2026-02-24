package com.sls.handbook.feature.dp.structural.adapter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun AdapterPatternRoute(
    modifier: Modifier = Modifier,
    viewModel: AdapterPatternViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AdapterPatternScreen(
        uiState = uiState,
        content = viewModel.content,
        onSystemToggle = viewModel::onSystemToggle,
        onTemperatureChange = viewModel::onTemperatureChanged,
        onDistanceChange = viewModel::onDistanceChanged,
        modifier = modifier,
    )
}
