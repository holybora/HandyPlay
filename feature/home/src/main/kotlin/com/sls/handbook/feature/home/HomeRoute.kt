package com.sls.handbook.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sls.handbook.core.model.Category

@Composable
fun HomeRoute(
    searchQuery: String,
    onCategoryClick: (Category) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(searchQuery) {
        viewModel.onSearchQueryChanged(searchQuery)
    }

    HomeScreen(
        uiState = uiState,
        onCategoryClick = onCategoryClick,
        modifier = modifier,
    )
}
