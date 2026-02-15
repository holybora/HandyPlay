package com.sls.handbook.feature.category

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.theapache64.rebugger.Rebugger

@Composable
fun CategoryRoute(
    searchQuery: String,
    onTopicClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CategoryViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Rebugger(
        composableName = "CategoryRoute",
        trackMap = mapOf(
            "searchQuery" to searchQuery,
            "uiState" to uiState,
        ),
    )

    LaunchedEffect(searchQuery) {
        viewModel.onSearchQueryChanged(searchQuery)
    }

    CategoryScreen(
        uiState = uiState,
        onTopicClick = onTopicClick,
        modifier = modifier,
    )
}
