package com.sls.handbook.feature.dp.behavioral.observer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ObserverRoute(
    modifier: Modifier = Modifier,
    viewModel: ObserverViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ObserverScreen(
        uiState = uiState,
        content = viewModel.content,
        onPriceChange = viewModel::onPriceChanged,
        onTogglePortfolio = viewModel::onTogglePortfolio,
        onToggleAlert = viewModel::onToggleAlert,
        onToggleChart = viewModel::onToggleChart,
        modifier = modifier,
    )
}
