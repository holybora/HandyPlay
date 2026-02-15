package com.sls.handbook.feature.ttlcache

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun TtlCacheRoute(
    modifier: Modifier = Modifier,
    viewModel: TtlCacheViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TtlCacheScreen(
        uiState = uiState,
        onTtlChange = viewModel::onTtlChange,
        onGetClick = viewModel::onGetClick,
        modifier = modifier,
    )
}
