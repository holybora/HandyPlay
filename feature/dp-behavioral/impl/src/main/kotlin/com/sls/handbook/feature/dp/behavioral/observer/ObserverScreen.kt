package com.sls.handbook.feature.dp.behavioral.observer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sls.handbook.core.designsystem.theme.HandyPlayTheme
import com.sls.handbook.core.model.PatternContent
import com.sls.handbook.core.ui.PatternScreenScaffold
import com.sls.handbook.core.ui.PatternTheoryTab

@Composable
fun ObserverScreen(
    uiState: ObserverUiState,
    content: PatternContent,
    onPriceChange: (Double) -> Unit,
    onTogglePortfolio: (Boolean) -> Unit,
    onToggleAlert: (Boolean) -> Unit,
    onToggleChart: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    PatternScreenScaffold(
        modifier = modifier,
        theoryTab = { PatternTheoryTab(content = content) },
        playgroundTab = {
            when (uiState) {
                is ObserverUiState.Idle -> PlaygroundContent(
                    uiState = uiState,
                    onPriceChange = onPriceChange,
                    onTogglePortfolio = onTogglePortfolio,
                    onToggleAlert = onToggleAlert,
                    onToggleChart = onToggleChart,
                )
            }
        },
    )
}

@Composable
private fun PlaygroundContent(
    uiState: ObserverUiState.Idle,
    onPriceChange: (Double) -> Unit,
    onTogglePortfolio: (Boolean) -> Unit,
    onToggleAlert: (Boolean) -> Unit,
    onToggleChart: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Text(
            text = "Stock Price Ticker",
            style = MaterialTheme.typography.headlineMedium,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            shape = RoundedCornerShape(12.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Publisher: Stock Price",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                )
                Text(
                    text = "${'$'}%.2f".format(uiState.stockPrice),
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary,
                )
                Slider(
                    value = uiState.stockPrice.toFloat(),
                    onValueChange = { onPriceChange(it.toDouble()) },
                    valueRange = 0f..200f,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Subscribers",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
        )

        Spacer(modifier = Modifier.height(8.dp))

        SubscriberCard(
            name = "Portfolio Tracker",
            value = uiState.portfolioValue,
            subscribed = uiState.portfolioSubscribed,
            onToggle = onTogglePortfolio,
        )

        Spacer(modifier = Modifier.height(8.dp))

        SubscriberCard(
            name = "Alert System",
            value = uiState.alertMessage,
            subscribed = uiState.alertSubscribed,
            onToggle = onToggleAlert,
        )

        Spacer(modifier = Modifier.height(8.dp))

        SubscriberCard(
            name = "Chart Widget",
            value = uiState.chartTrend,
            subscribed = uiState.chartSubscribed,
            onToggle = onToggleChart,
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun SubscriberCard(
    name: String,
    value: String,
    subscribed: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (subscribed) {
                MaterialTheme.colorScheme.secondaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            },
        ),
        shape = RoundedCornerShape(12.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                    )
                    Text(
                        text = if (subscribed) "Subscribed" else "Unsubscribed",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Switch(checked = subscribed, onCheckedChange = onToggle)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = if (subscribed) {
                    MaterialTheme.colorScheme.onSecondaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ObserverScreenPreview() {
    HandyPlayTheme {
        ObserverScreen(
            uiState = ObserverUiState.Idle(),
            content = PatternContent("Observer", "Behavioral", "Desc", listOf("Use"), "Ex", "Code"),
            onPriceChange = {},
            onTogglePortfolio = {},
            onToggleAlert = {},
            onToggleChart = {},
        )
    }
}
