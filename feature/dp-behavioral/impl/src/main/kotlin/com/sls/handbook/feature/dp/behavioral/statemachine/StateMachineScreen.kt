package com.sls.handbook.feature.dp.behavioral.statemachine

import androidx.compose.foundation.border
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
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
fun StateMachineScreen(
    uiState: StateMachineUiState,
    content: PatternContent,
    onInsertCoin: () -> Unit,
    onSelectItem: () -> Unit,
    onDispense: () -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PatternScreenScaffold(
        modifier = modifier,
        theoryTab = { PatternTheoryTab(content = content) },
        playgroundTab = {
            when (uiState) {
                is StateMachineUiState.Idle -> PlaygroundContent(
                    uiState = uiState,
                    onInsertCoin = onInsertCoin,
                    onSelectItem = onSelectItem,
                    onDispense = onDispense,
                    onReset = onReset,
                )
            }
        },
    )
}

@Composable
private fun PlaygroundContent(
    uiState: StateMachineUiState.Idle,
    onInsertCoin: () -> Unit,
    onSelectItem: () -> Unit,
    onDispense: () -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Text(
            text = "Vending Machine",
            style = MaterialTheme.typography.headlineMedium,
        )

        Spacer(modifier = Modifier.height(16.dp))

        StateDiagram(currentState = uiState.currentState)

        Spacer(modifier = Modifier.height(16.dp))

        StatusCard(uiState = uiState)

        Spacer(modifier = Modifier.height(16.dp))

        ActionButtons(
            uiState = uiState,
            onInsertCoin = onInsertCoin,
            onSelectItem = onSelectItem,
            onDispense = onDispense,
            onReset = onReset,
        )

        Spacer(modifier = Modifier.height(16.dp))

        TransitionLog(log = uiState.log)

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun StateDiagram(
    currentState: VendingState,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = "State Diagram",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
        )

        Spacer(modifier = Modifier.height(8.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            VendingState.entries.forEach { state ->
                val isActive = currentState == state
                Surface(
                    color = if (isActive) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    },
                    contentColor = if (isActive) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    shape = RoundedCornerShape(12.dp),
                    modifier = if (isActive) {
                        Modifier.border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(12.dp),
                        )
                    } else {
                        Modifier
                    },
                ) {
                    Text(
                        text = state.displayName,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusCard(
    uiState: StateMachineUiState.Idle,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        shape = RoundedCornerShape(12.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Status",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                )
                Text(
                    text = uiState.currentState.displayName,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Coins: ${uiState.coins} | Items: ${uiState.itemCount}",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
private fun ActionButtons(
    uiState: StateMachineUiState.Idle,
    onInsertCoin: () -> Unit,
    onSelectItem: () -> Unit,
    onDispense: () -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = "Actions",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = onInsertCoin,
                modifier = Modifier.weight(1f),
                enabled = uiState.currentState != VendingState.SOLD_OUT,
            ) {
                Text("Insert Coin")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = onSelectItem,
                modifier = Modifier.weight(1f),
                enabled = uiState.currentState == VendingState.HAS_COIN,
            ) {
                Text("Select Item")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = onDispense,
                modifier = Modifier.weight(1f),
                enabled = uiState.currentState == VendingState.DISPENSING,
            ) {
                Text("Dispense")
            }
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedButton(
                onClick = onReset,
                modifier = Modifier.weight(1f),
            ) {
                Text("Reset")
            }
        }
    }
}

@Composable
private fun TransitionLog(
    log: List<String>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = "Transition Log",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(8.dp),
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                log.reversed().forEach { entry ->
                    Text(
                        text = entry,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StateMachineScreenPreview() {
    HandyPlayTheme {
        StateMachineScreen(
            uiState = StateMachineUiState.Idle(
                currentState = VendingState.HAS_COIN,
                coins = 1,
                log = listOf("Machine ready.", "Coin inserted."),
            ),
            content = PatternContent("State Machine", "Behavioral", "Desc", listOf("Use"), "Ex", "Code"),
            onInsertCoin = {},
            onSelectItem = {},
            onDispense = {},
            onReset = {},
        )
    }
}
