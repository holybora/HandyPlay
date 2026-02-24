package com.sls.handbook.feature.dp.creational.prototype

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sls.handbook.core.designsystem.theme.HandyPlayTheme
import com.sls.handbook.core.model.PatternContent
import com.sls.handbook.core.ui.PatternScreenScaffold
import com.sls.handbook.core.ui.PatternTheoryTab

private val presetColors = listOf(
    "Blue" to 0xFF2196F3,
    "Red" to 0xFFF44336,
    "Green" to 0xFF4CAF50,
    "Orange" to 0xFFFF9800,
    "Purple" to 0xFF9C27B0,
)

@Composable
fun PrototypeScreen(
    uiState: PrototypeUiState,
    content: PatternContent,
    onClone: () -> Unit,
    onSelectClone: (Int?) -> Unit,
    onCloneColorChange: (Int, String, Long) -> Unit,
    onCloneSizeChange: (Int, Int) -> Unit,
    onOriginalTypeChange: (ShapeType) -> Unit,
    modifier: Modifier = Modifier,
) {
    PatternScreenScaffold(
        modifier = modifier,
        theoryTab = { PatternTheoryTab(content = content) },
        playgroundTab = {
            when (uiState) {
                is PrototypeUiState.Idle -> PlaygroundContent(
                    uiState = uiState,
                    onClone = onClone,
                    onSelectClone = onSelectClone,
                    onCloneColorChange = onCloneColorChange,
                    onCloneSizeChange = onCloneSizeChange,
                    onOriginalTypeChange = onOriginalTypeChange,
                )
            }
        },
    )
}

@Composable
private fun PlaygroundContent(
    uiState: PrototypeUiState.Idle,
    onClone: () -> Unit,
    onSelectClone: (Int?) -> Unit,
    onCloneColorChange: (Int, String, Long) -> Unit,
    onCloneSizeChange: (Int, Int) -> Unit,
    onOriginalTypeChange: (ShapeType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Text(
            text = "Shape Cloner",
            style = MaterialTheme.typography.headlineMedium,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Original Prototype",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
        )

        Spacer(modifier = Modifier.height(8.dp))

        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            ShapeType.entries.forEachIndexed { index, type ->
                SegmentedButton(
                    selected = uiState.originalShape.type == type,
                    onClick = { onOriginalTypeChange(type) },
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = ShapeType.entries.size,
                    ),
                ) {
                    Text(type.displayName)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        ShapeDisplay(shape = uiState.originalShape)

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onClone,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Clone Original")
        }

        if (uiState.clones.isNotEmpty()) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Clones (${uiState.clones.size})",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
            )

            Spacer(modifier = Modifier.height(8.dp))

            uiState.clones.forEachIndexed { index, clone ->
                CloneCard(
                    clone = clone,
                    index = index,
                    isSelected = uiState.selectedCloneIndex == index,
                    onSelect = { onSelectClone(if (uiState.selectedCloneIndex == index) null else index) },
                    onColorChange = { name, value -> onCloneColorChange(index, name, value) },
                    onSizeChange = { size -> onCloneSizeChange(index, size) },
                )

                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun ShapeDisplay(
    shape: ShapePrototype,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(12.dp),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val clipShape = when (shape.type) {
                ShapeType.CIRCLE -> CircleShape
                ShapeType.SQUARE -> RoundedCornerShape(4.dp)
                ShapeType.TRIANGLE -> RoundedCornerShape(4.dp)
            }
            Box(
                modifier = Modifier
                    .size((shape.size / 2).coerceIn(24, 60).dp)
                    .clip(clipShape)
                    .background(Color(shape.colorValue)),
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(text = shape.type.displayName, style = MaterialTheme.typography.titleSmall)
                Text(
                    text = "Color: ${shape.colorName} | Size: ${shape.size}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CloneCard(
    clone: ShapePrototype,
    index: Int,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onColorChange: (String, Long) -> Unit,
    onSizeChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.secondaryContainer
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            },
        ),
        shape = RoundedCornerShape(12.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val clipShape = when (clone.type) {
                    ShapeType.CIRCLE -> CircleShape
                    ShapeType.SQUARE -> RoundedCornerShape(4.dp)
                    ShapeType.TRIANGLE -> RoundedCornerShape(4.dp)
                }
                Box(
                    modifier = Modifier
                        .size((clone.size / 2).coerceIn(24, 60).dp)
                        .clip(clipShape)
                        .background(Color(clone.colorValue)),
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Clone #${index + 1}",
                        style = MaterialTheme.typography.titleSmall,
                    )
                    Text(
                        text = "${clone.type.displayName} | ${clone.colorName} | Size: ${clone.size}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            AnimatedVisibility(visible = isSelected) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    Text(
                        text = "Color",
                        style = MaterialTheme.typography.labelMedium,
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        presetColors.forEach { (name, value) ->
                            FilterChip(
                                selected = clone.colorName == name,
                                onClick = { onColorChange(name, value) },
                                label = { Text(name) },
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Size: ${clone.size}",
                        style = MaterialTheme.typography.labelMedium,
                    )

                    Slider(
                        value = clone.size.toFloat(),
                        onValueChange = { onSizeChange(it.toInt()) },
                        valueRange = 40f..200f,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PrototypeScreenPreview() {
    HandyPlayTheme {
        PrototypeScreen(
            uiState = PrototypeUiState.Idle(
                clones = listOf(
                    ShapePrototype(ShapeType.CIRCLE, "Red", 0xFFF44336, 80),
                    ShapePrototype(ShapeType.CIRCLE, "Blue", 0xFF2196F3, 120),
                ),
                selectedCloneIndex = 0,
            ),
            content = PatternContent("Prototype", "Creational", "Desc", listOf("Use"), "Ex", "Code"),
            onClone = {},
            onSelectClone = {},
            onCloneColorChange = { _, _, _ -> },
            onCloneSizeChange = { _, _ -> },
            onOriginalTypeChange = {},
        )
    }
}
