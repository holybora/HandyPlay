package com.sls.handbook.feature.listvssequence

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ListVsSequenceRoute(
    modifier: Modifier = Modifier,
    viewModel: ListVsSequenceViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ListVsSequenceScreen(
        uiState = uiState,
        onStartClick = viewModel::onStartClick,
        modifier = modifier,
    )
}
