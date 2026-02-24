package com.sls.handbook.feature.dp.behavioral.strategy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
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
fun StrategyScreen(
    uiState: StrategyUiState,
    content: PatternContent,
    onStrategySelect: (SortStrategyType) -> Unit,
    onSort: () -> Unit,
    onShuffle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PatternScreenScaffold(
        modifier = modifier,
        theoryTab = { PatternTheoryTab(content = content) },
        playgroundTab = {
            when (uiState) {
                is StrategyUiState.Idle -> PlaygroundContent(
                    uiState = uiState,
                    onStrategySelect = onStrategySelect,
                    onSort = onSort,
                    onShuffle = onShuffle,
                )
            }
        },
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PlaygroundContent(
    uiState: StrategyUiState.Idle,
    onStrategySelect: (SortStrategyType) -> Unit,
    onSort: () -> Unit,
    onShuffle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Text(
            text = "Sort Visualizer",
            style = MaterialTheme.typography.headlineMedium,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Input",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
        )

        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            uiState.numbers.forEach { number ->
                AssistChip(
                    onClick = {},
                    label = { Text(number.toString()) },
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Strategy",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
        )

        Spacer(modifier = Modifier.height(8.dp))

        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            SortStrategyType.entries.forEachIndexed { index, strategy ->
                SegmentedButton(
                    selected = uiState.selectedStrategy == strategy,
                    onClick = { onStrategySelect(strategy) },
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = SortStrategyType.entries.size,
                    ),
                ) {
                    Text(strategy.displayName, style = MaterialTheme.typography.labelSmall)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = onSort,
                modifier = Modifier.weight(1f),
            ) {
                Text("Sort")
            }
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedButton(
                onClick = onShuffle,
                modifier = Modifier.weight(1f),
            ) {
                Text("Shuffle")
            }
        }

        uiState.sortedNumbers?.let { sorted ->
            Spacer(modifier = Modifier.height(16.dp))

            ResultCard(
                sortedNumbers = sorted,
                stepCount = uiState.stepCount,
                comparisonCount = uiState.comparisonCount,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ResultCard(
    sortedNumbers: List<Int>,
    stepCount: Int,
    comparisonCount: Int,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        shape = RoundedCornerShape(12.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Result",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
            )

            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                sortedNumbers.forEach { number ->
                    AssistChip(
                        onClick = {},
                        label = { Text(number.toString()) },
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Swaps: $stepCount | Comparisons: $comparisonCount",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StrategyScreenPreview() {
    HandyPlayTheme {
        StrategyScreen(
            uiState = StrategyUiState.Idle(
                sortedNumbers = listOf(3, 9, 10, 27, 38, 43, 82),
                stepCount = 12,
                comparisonCount = 21,
            ),
            content = PatternContent("Strategy", "Behavioral", "Desc", listOf("Use"), "Ex", "Code"),
            onStrategySelect = {},
            onSort = {},
            onShuffle = {},
        )
    }
}
