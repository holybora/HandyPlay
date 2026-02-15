package com.sls.handbook.feature.ttlcache

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.theapache64.rebugger.Rebugger

@Composable
fun TtlCacheRoute(
    modifier: Modifier = Modifier,
    viewModel: TtlCacheViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Rebugger(
        composableName = "TtlCacheRoute",
        trackMap = mapOf(
            "uiState" to uiState,
        ),
    )

    TtlCacheScreen(
        uiState = uiState,
        onTtlChange = viewModel::onTtlChange,
        onGetClick = viewModel::onGetClick,
        modifier = modifier,
    )
}
