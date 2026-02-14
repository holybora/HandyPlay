package com.sls.handbook.feature.welcome

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun WelcomeRoute(
    onGetStarted: () -> Unit,
    modifier: Modifier = Modifier
) {
    WelcomeScreen(
        onGetStarted = onGetStarted,
        modifier = modifier
    )
}
