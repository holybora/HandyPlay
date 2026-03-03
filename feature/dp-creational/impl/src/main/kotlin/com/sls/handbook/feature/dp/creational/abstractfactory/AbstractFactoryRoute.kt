package com.sls.handbook.feature.dp.creational.abstractfactory

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun AbstractFactoryRoute(
    modifier: Modifier = Modifier,
    viewModel: AbstractFactoryViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AbstractFactoryScreen(
        uiState = uiState,
        content = viewModel.content,
        onThemeSelect = viewModel::onThemeSelected,
        modifier = modifier,
    )
}
