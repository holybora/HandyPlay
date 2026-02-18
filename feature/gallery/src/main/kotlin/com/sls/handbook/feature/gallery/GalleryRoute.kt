package com.sls.handbook.feature.gallery

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.theapache64.rebugger.Rebugger

@Composable
fun GalleryRoute(
    modifier: Modifier = Modifier,
    viewModel: GalleryViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Rebugger(
        composableName = "GalleryRoute",
        trackMap = mapOf(
            "uiState" to uiState,
        ),
    )

    GalleryScreen(
        uiState = uiState,
        modifier = modifier,
    )
}
