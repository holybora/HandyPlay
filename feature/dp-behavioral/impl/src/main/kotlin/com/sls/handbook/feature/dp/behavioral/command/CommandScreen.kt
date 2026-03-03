package com.sls.handbook.feature.dp.behavioral.command

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sls.handbook.core.designsystem.theme.HandyPlayTheme
import com.sls.handbook.core.model.PatternContent
import com.sls.handbook.core.ui.PatternScreenScaffold
import com.sls.handbook.core.ui.PatternTheoryTab

@Composable
fun CommandScreen(
    uiState: CommandUiState,
    content: PatternContent,
    onToggleBold: () -> Unit,
    onToggleItalic: () -> Unit,
    onToggleUnderline: () -> Unit,
    onTextChange: (String) -> Unit,
    onUndo: () -> Unit,
    onRedo: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PatternScreenScaffold(
        modifier = modifier,
        theoryTab = { PatternTheoryTab(content = content) },
        playgroundTab = {
            when (uiState) {
                is CommandUiState.Idle -> PlaygroundContent(
                    uiState = uiState,
                    onToggleBold = onToggleBold,
                    onToggleItalic = onToggleItalic,
                    onToggleUnderline = onToggleUnderline,
                    onTextChange = onTextChange,
                    onUndo = onUndo,
                    onRedo = onRedo,
                )
            }
        },
    )
}

@Composable
private fun PlaygroundContent(
    uiState: CommandUiState.Idle,
    onToggleBold: () -> Unit,
    onToggleItalic: () -> Unit,
    onToggleUnderline: () -> Unit,
    onTextChange: (String) -> Unit,
    onUndo: () -> Unit,
    onRedo: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Text(
            text = "Text Editor",
            style = MaterialTheme.typography.headlineMedium,
        )

        Spacer(modifier = Modifier.height(16.dp))

        CommandChips(
            uiState = uiState,
            onToggleBold = onToggleBold,
            onToggleItalic = onToggleItalic,
            onToggleUnderline = onToggleUnderline,
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.text,
            onValueChange = onTextChange,
            label = { Text("Text") },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextPreviewCard(uiState = uiState)

        Spacer(modifier = Modifier.height(16.dp))

        UndoRedoButtons(
            uiState = uiState,
            onUndo = onUndo,
            onRedo = onRedo,
        )

        if (uiState.commandHistory.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            CommandHistoryPanel(commandHistory = uiState.commandHistory)
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun CommandChips(
    uiState: CommandUiState.Idle,
    onToggleBold: () -> Unit,
    onToggleItalic: () -> Unit,
    onToggleUnderline: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = "Commands",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
                selected = uiState.isBold,
                onClick = onToggleBold,
                label = { Text("Bold") },
            )
            FilterChip(
                selected = uiState.isItalic,
                onClick = onToggleItalic,
                label = { Text("Italic") },
            )
            FilterChip(
                selected = uiState.isUnderline,
                onClick = onToggleUnderline,
                label = { Text("Underline") },
            )
        }
    }
}

@Composable
private fun TextPreviewCard(
    uiState: CommandUiState.Idle,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = "Preview",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ),
            shape = RoundedCornerShape(12.dp),
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        SpanStyle(
                            fontWeight = if (uiState.isBold) FontWeight.Bold else FontWeight.Normal,
                            fontStyle = if (uiState.isItalic) FontStyle.Italic else FontStyle.Normal,
                            textDecoration = if (uiState.isUnderline) {
                                TextDecoration.Underline
                            } else {
                                TextDecoration.None
                            },
                        ),
                    ) {
                        append(uiState.text)
                    }
                },
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}

@Composable
private fun UndoRedoButtons(
    uiState: CommandUiState.Idle,
    onUndo: () -> Unit,
    onRedo: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier.fillMaxWidth()) {
        Button(
            onClick = onUndo,
            modifier = Modifier.weight(1f),
            enabled = uiState.undoStack.isNotEmpty(),
        ) {
            Text("Undo")
        }
        Spacer(modifier = Modifier.width(8.dp))
        OutlinedButton(
            onClick = onRedo,
            modifier = Modifier.weight(1f),
            enabled = uiState.redoStack.isNotEmpty(),
        ) {
            Text("Redo")
        }
    }
}

@Composable
private fun CommandHistoryPanel(
    commandHistory: List<String>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = "Command History",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(8.dp),
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                commandHistory.reversed().forEach { entry ->
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
private fun CommandScreenPreview() {
    HandyPlayTheme {
        CommandScreen(
            uiState = CommandUiState.Idle(
                isBold = true,
                commandHistory = listOf("Execute: Bold"),
                undoStack = listOf(TextCommand.ToggleBold(false)),
            ),
            content = PatternContent("Command", "Behavioral", "Desc", listOf("Use"), "Ex", "Code"),
            onToggleBold = {},
            onToggleItalic = {},
            onToggleUnderline = {},
            onTextChange = {},
            onUndo = {},
            onRedo = {},
        )
    }
}
