package com.sls.handbook.feature.dp.creational.abstractfactory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import com.sls.handbook.core.designsystem.theme.HandyPlayTheme
import com.sls.handbook.core.model.PatternContent
import com.sls.handbook.core.ui.PatternScreenScaffold
import com.sls.handbook.core.ui.PatternTheoryTab

@Composable
fun AbstractFactoryScreen(
    uiState: AbstractFactoryUiState,
    content: PatternContent,
    onThemeSelect: (ThemeFamily) -> Unit,
    modifier: Modifier = Modifier,
) {
    PatternScreenScaffold(
        modifier = modifier,
        theoryTab = { PatternTheoryTab(content = content) },
        playgroundTab = {
            when (uiState) {
                is AbstractFactoryUiState.Idle -> PlaygroundContent(
                    uiState = uiState,
                    onThemeSelect = onThemeSelect,
                )
            }
        },
    )
}

@Composable
private fun PlaygroundContent(
    uiState: AbstractFactoryUiState.Idle,
    onThemeSelect: (ThemeFamily) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Text(
            text = "UI Theme Factory",
            style = MaterialTheme.typography.headlineMedium,
        )

        Spacer(modifier = Modifier.height(16.dp))

        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            ThemeFamily.entries.forEachIndexed { index, theme ->
                SegmentedButton(
                    selected = uiState.selectedTheme == theme,
                    onClick = { onThemeSelect(theme) },
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = ThemeFamily.entries.size,
                    ),
                ) {
                    Text(theme.displayName)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        ComponentPreviewCard(component = uiState.button)

        Spacer(modifier = Modifier.height(12.dp))

        ComponentPreviewCard(component = uiState.textField)

        Spacer(modifier = Modifier.height(12.dp))

        ComponentPreviewCard(component = uiState.card)

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun ComponentPreviewCard(
    component: UiComponentDisplay,
    modifier: Modifier = Modifier,
) {
    val color = try {
        Color(component.colorHex.toColorInt())
    } catch (_: IllegalArgumentException) {
        MaterialTheme.colorScheme.primary
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        shape = RoundedCornerShape(12.dp),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(color),
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = component.name,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = component.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AbstractFactoryScreenPreview() {
    HandyPlayTheme {
        AbstractFactoryScreen(
            uiState = AbstractFactoryUiState.Idle(),
            content = PatternContent("Abstract Factory", "Creational", "Desc", listOf("Use"), "Ex", "Code"),
            onThemeSelect = {},
        )
    }
}
