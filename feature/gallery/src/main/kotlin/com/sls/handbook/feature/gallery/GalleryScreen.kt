package com.sls.handbook.feature.gallery

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sls.handbook.core.designsystem.theme.HandyPlayTheme
import com.theapache64.rebugger.Rebugger

@Composable
fun GalleryScreen(
    uiState: GalleryUiState,
    modifier: Modifier = Modifier,
) {
    Rebugger(
        composableName = "GalleryScreen",
        trackMap = mapOf(
            "uiState" to uiState,
        ),
    )

    when (uiState) {
        is GalleryUiState.Content -> GalleryContent(
            uiState = uiState,
            modifier = modifier,
        )
    }
}

@Composable
private fun GalleryContent(
    uiState: GalleryUiState.Content,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Text(
            text = uiState.title,
            style = MaterialTheme.typography.headlineMedium,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = uiState.description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GalleryScreenPreview() {
    HandyPlayTheme {
        GalleryScreen(
            uiState = GalleryUiState.Content(),
        )
    }
}
