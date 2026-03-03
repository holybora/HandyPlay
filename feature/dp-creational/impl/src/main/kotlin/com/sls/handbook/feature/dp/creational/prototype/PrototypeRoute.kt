package com.sls.handbook.feature.dp.creational.prototype

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun PrototypeRoute(
    modifier: Modifier = Modifier,
    viewModel: PrototypeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PrototypeScreen(
        uiState = uiState,
        content = viewModel.content,
        onClone = viewModel::onClone,
        onSelectClone = viewModel::onSelectClone,
        onCloneColorChange = viewModel::onCloneColorChanged,
        onCloneSizeChange = viewModel::onCloneSizeChanged,
        onOriginalTypeChange = viewModel::onOriginalTypeChanged,
        modifier = modifier,
    )
}
