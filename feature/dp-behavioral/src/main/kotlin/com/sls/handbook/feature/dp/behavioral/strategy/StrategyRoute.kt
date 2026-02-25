package com.sls.handbook.feature.dp.behavioral.strategy

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun StrategyRoute(
    modifier: Modifier = Modifier,
    viewModel: StrategyViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    StrategyScreen(
        uiState = uiState,
        content = viewModel.content,
        onStrategySelect = viewModel::onStrategySelected,
        onSort = viewModel::onSort,
        onShuffle = viewModel::onShuffle,
        modifier = modifier,
    )
}
