package com.sls.handbook.feature.category

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun CategoryRoute(
    onTopicClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CategoryViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CategoryScreen(
        uiState = uiState,
        onSearchQueryChange = viewModel::onSearchQueryChanged,
        onTopicClick = onTopicClick,
        modifier = modifier,
    )
}
