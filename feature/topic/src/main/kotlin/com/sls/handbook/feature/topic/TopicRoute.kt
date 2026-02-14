package com.sls.handbook.feature.topic

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun TopicRoute(
    modifier: Modifier = Modifier,
    viewModel: TopicViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TopicScreen(
        uiState = uiState,
        modifier = modifier,
    )
}
