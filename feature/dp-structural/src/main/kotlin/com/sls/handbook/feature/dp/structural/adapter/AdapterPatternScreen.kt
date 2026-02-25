package com.sls.handbook.feature.dp.structural.adapter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sls.handbook.core.designsystem.theme.HandyPlayTheme
import com.sls.handbook.core.model.PatternContent
import com.sls.handbook.core.ui.PatternScreenScaffold
import com.sls.handbook.core.ui.PatternTheoryTab

@Composable
fun AdapterPatternScreen(
    uiState: AdapterPatternUiState,
    content: PatternContent,
    onSystemToggle: (Boolean) -> Unit,
    onTemperatureChange: (Double) -> Unit,
    onDistanceChange: (Double) -> Unit,
    modifier: Modifier = Modifier,
) {
    PatternScreenScaffold(
        modifier = modifier,
        theoryTab = { PatternTheoryTab(content = content) },
        playgroundTab = {
            when (uiState) {
                is AdapterPatternUiState.Idle -> PlaygroundContent(
                    uiState = uiState,
                    onSystemToggle = onSystemToggle,
                    onTemperatureChange = onTemperatureChange,
                    onDistanceChange = onDistanceChange,
                )
            }
        },
    )
}

@Composable
private fun PlaygroundContent(
    uiState: AdapterPatternUiState.Idle,
    onSystemToggle: (Boolean) -> Unit,
    onTemperatureChange: (Double) -> Unit,
    onDistanceChange: (Double) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Text(
            text = "API Response Adapter",
            style = MaterialTheme.typography.headlineMedium,
        )

        Spacer(modifier = Modifier.height(16.dp))

        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            SegmentedButton(
                selected = uiState.useMetric,
                onClick = { onSystemToggle(true) },
                shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
            ) {
                Text("Metric")
            }
            SegmentedButton(
                selected = !uiState.useMetric,
                onClick = { onSystemToggle(false) },
                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
            ) {
                Text("Imperial")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Adjust Raw Values",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Temperature: %.0f°F".format(uiState.rawData.temperatureFahrenheit),
            style = MaterialTheme.typography.bodyMedium,
        )
        Slider(
            value = uiState.rawData.temperatureFahrenheit.toFloat(),
            onValueChange = { onTemperatureChange(it.toDouble()) },
            valueRange = -20f..120f,
            modifier = Modifier.fillMaxWidth(),
        )

        Text(
            text = "Distance: %.1f mi".format(uiState.rawData.distanceMiles),
            style = MaterialTheme.typography.bodyMedium,
        )
        Slider(
            value = uiState.rawData.distanceMiles.toFloat(),
            onValueChange = { onDistanceChange(it.toDouble()) },
            valueRange = 0f..100f,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            DataCard(
                title = "Raw Data",
                items = listOf(
                    "Temp" to "%.1f °F".format(uiState.rawData.temperatureFahrenheit),
                    "Dist" to "%.1f mi".format(uiState.rawData.distanceMiles),
                    "Speed" to "%.1f mph".format(uiState.rawData.speedMph),
                    "Press" to "%.2f inHg".format(uiState.rawData.pressureInHg),
                ),
                modifier = Modifier.weight(1f),
            )

            Spacer(modifier = Modifier.width(8.dp))

            DataCard(
                title = "Adapted",
                items = listOf(
                    "Temp" to uiState.adaptedData.temperature,
                    "Dist" to uiState.adaptedData.distance,
                    "Speed" to uiState.adaptedData.speed,
                    "Press" to uiState.adaptedData.pressure,
                ),
                modifier = Modifier.weight(1f),
                isAdapted = true,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun DataCard(
    title: String,
    items: List<Pair<String, String>>,
    modifier: Modifier = Modifier,
    isAdapted: Boolean = false,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = if (isAdapted) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            },
        ),
        shape = RoundedCornerShape(12.dp),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
            )

            Spacer(modifier = Modifier.height(8.dp))

            items.forEach { (label, value) ->
                Text(
                    text = "$label: $value",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(vertical = 2.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AdapterPatternScreenPreview() {
    HandyPlayTheme {
        AdapterPatternScreen(
            uiState = AdapterPatternUiState.Idle(),
            content = PatternContent("Adapter", "Structural", "Desc", listOf("Use"), "Ex", "Code"),
            onSystemToggle = {},
            onTemperatureChange = {},
            onDistanceChange = {},
        )
    }
}
