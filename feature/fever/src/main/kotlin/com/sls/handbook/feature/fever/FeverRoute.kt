package com.sls.handbook.feature.fever

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sls.handbook.feature.fever.theme.FeverTheme
import com.theapache64.rebugger.Rebugger

@Composable
fun FeverRoute(
    modifier: Modifier = Modifier,
    viewModel: FeverViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Rebugger(composableName = "FeverRoute", trackMap = mapOf("uiState" to uiState))

    FeverTheme {
        FeverScreen(
            uiState = uiState,
            onEvent = viewModel::onEvent,
            modifier = modifier,
        )
    }
}
