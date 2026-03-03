package com.sls.handbook.feature.welcome

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun WelcomeRoute(
    onStart: () -> Unit,
    modifier: Modifier = Modifier,
) {
    WelcomeScreen(
        modifier = modifier,
        onStart = onStart,
    )
}
