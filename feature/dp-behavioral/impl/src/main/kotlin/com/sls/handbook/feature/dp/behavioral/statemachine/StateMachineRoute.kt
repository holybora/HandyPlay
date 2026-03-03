package com.sls.handbook.feature.dp.behavioral.statemachine

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun StateMachineRoute(
    modifier: Modifier = Modifier,
    viewModel: StateMachineViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    StateMachineScreen(
        uiState = uiState,
        content = viewModel.content,
        onInsertCoin = viewModel::onInsertCoin,
        onSelectItem = viewModel::onSelectItem,
        onDispense = viewModel::onDispense,
        onReset = viewModel::onReset,
        modifier = modifier,
    )
}
