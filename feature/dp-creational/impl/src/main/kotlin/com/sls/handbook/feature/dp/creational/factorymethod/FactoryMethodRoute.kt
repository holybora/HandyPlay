package com.sls.handbook.feature.dp.creational.factorymethod

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun FactoryMethodRoute(
    modifier: Modifier = Modifier,
    viewModel: FactoryMethodViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    FactoryMethodScreen(
        uiState = uiState,
        content = viewModel.content,
        onTypeSelect = viewModel::onTypeSelected,
        onCreateClick = viewModel::onCreateClick,
        modifier = modifier,
    )
}
