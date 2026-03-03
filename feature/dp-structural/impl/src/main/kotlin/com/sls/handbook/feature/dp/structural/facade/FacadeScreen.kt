package com.sls.handbook.feature.dp.structural.facade

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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sls.handbook.core.designsystem.theme.HandyPlayTheme
import com.sls.handbook.core.model.PatternContent
import com.sls.handbook.core.ui.PatternScreenScaffold
import com.sls.handbook.core.ui.PatternTheoryTab

@Composable
fun FacadeScreen(
    uiState: FacadeUiState,
    content: PatternContent,
    onToggleFacade: (Boolean) -> Unit,
    onWatchMovie: () -> Unit,
    onStopMovie: () -> Unit,
    onToggleProjector: (Boolean) -> Unit,
    onToggleSound: (Boolean) -> Unit,
    onToggleLights: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    PatternScreenScaffold(
        modifier = modifier,
        theoryTab = { PatternTheoryTab(content = content) },
        playgroundTab = {
            when (uiState) {
                is FacadeUiState.Idle -> PlaygroundContent(
                    uiState = uiState,
                    onToggleFacade = onToggleFacade,
                    onWatchMovie = onWatchMovie,
                    onStopMovie = onStopMovie,
                    onToggleProjector = onToggleProjector,
                    onToggleSound = onToggleSound,
                    onToggleLights = onToggleLights,
                )
            }
        },
    )
}

@Composable
private fun PlaygroundContent(
    uiState: FacadeUiState.Idle,
    onToggleFacade: (Boolean) -> Unit,
    onWatchMovie: () -> Unit,
    onStopMovie: () -> Unit,
    onToggleProjector: (Boolean) -> Unit,
    onToggleSound: (Boolean) -> Unit,
    onToggleLights: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Text(
            text = "Home Theater",
            style = MaterialTheme.typography.headlineMedium,
        )

        Spacer(modifier = Modifier.height(16.dp))

        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            SegmentedButton(
                selected = uiState.useFacade,
                onClick = { onToggleFacade(true) },
                shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
            ) {
                Text("Facade Mode")
            }
            SegmentedButton(
                selected = !uiState.useFacade,
                onClick = { onToggleFacade(false) },
                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
            ) {
                Text("Manual Mode")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        SubsystemCard("Projector", if (uiState.projectorOn) "ON" else "OFF", uiState.projectorOn)
        Spacer(modifier = Modifier.height(8.dp))
        SubsystemCard("Sound System", if (uiState.soundSurround) "Surround" else "OFF", uiState.soundSurround)
        Spacer(modifier = Modifier.height(8.dp))
        SubsystemCard("Lights", if (uiState.lightsDimmed) "Dimmed" else "Bright", uiState.lightsDimmed)

        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.useFacade) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = onWatchMovie,
                    modifier = Modifier.weight(1f),
                ) {
                    Text("Watch Movie")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onStopMovie,
                    modifier = Modifier.weight(1f),
                ) {
                    Text("Stop Movie")
                }
            }
        } else {
            SwitchRow("Projector", uiState.projectorOn, onToggleProjector)
            SwitchRow("Surround Sound", uiState.soundSurround, onToggleSound)
            SwitchRow("Dim Lights", uiState.lightsDimmed, onToggleLights)
        }

        if (uiState.log.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Operation Log",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(8.dp),
            ) {
                Text(
                    text = uiState.log.joinToString(separator = "\n"),
                    style = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace),
                    modifier = Modifier.padding(12.dp),
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun SubsystemCard(
    name: String,
    status: String,
    isActive: Boolean,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isActive) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            },
        ),
        shape = RoundedCornerShape(12.dp),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier.weight(1f),
            )
            Text(
                text = status,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isActive) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
            )
        }
    }
}

@Composable
private fun SwitchRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f),
        )
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Preview(showBackground = true)
@Composable
private fun FacadeScreenPreview() {
    HandyPlayTheme {
        FacadeScreen(
            uiState = FacadeUiState.Idle(
                projectorOn = true,
                soundSurround = true,
                lightsDimmed = true,
                log = listOf("Facade: watchMovie() called"),
            ),
            content = PatternContent("Facade", "Structural", "Desc", listOf("Use"), "Ex", "Code"),
            onToggleFacade = {},
            onWatchMovie = {},
            onStopMovie = {},
            onToggleProjector = {},
            onToggleSound = {},
            onToggleLights = {},
        )
    }
}
