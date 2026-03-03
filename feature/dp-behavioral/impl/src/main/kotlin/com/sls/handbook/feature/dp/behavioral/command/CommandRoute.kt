package com.sls.handbook.feature.dp.behavioral.command

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun CommandRoute(
    modifier: Modifier = Modifier,
    viewModel: CommandViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CommandScreen(
        uiState = uiState,
        content = viewModel.content,
        onToggleBold = viewModel::onToggleBold,
        onToggleItalic = viewModel::onToggleItalic,
        onToggleUnderline = viewModel::onToggleUnderline,
        onTextChange = viewModel::onTextChanged,
        onUndo = viewModel::onUndo,
        onRedo = viewModel::onRedo,
        modifier = modifier,
    )
}
