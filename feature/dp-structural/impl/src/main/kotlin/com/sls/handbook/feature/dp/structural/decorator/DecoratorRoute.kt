package com.sls.handbook.feature.dp.structural.decorator

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun DecoratorRoute(
    modifier: Modifier = Modifier,
    viewModel: DecoratorViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DecoratorScreen(
        uiState = uiState,
        content = viewModel.content,
        onToggleMilk = viewModel::onToggleMilk,
        onToggleSugar = viewModel::onToggleSugar,
        onToggleWhippedCream = viewModel::onToggleWhippedCream,
        onToggleCaramel = viewModel::onToggleCaramel,
        modifier = modifier,
    )
}
