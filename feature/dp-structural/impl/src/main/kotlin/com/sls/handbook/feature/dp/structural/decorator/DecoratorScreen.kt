package com.sls.handbook.feature.dp.structural.decorator

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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
fun DecoratorScreen(
    uiState: DecoratorUiState,
    content: PatternContent,
    onToggleMilk: (Boolean) -> Unit,
    onToggleSugar: (Boolean) -> Unit,
    onToggleWhippedCream: (Boolean) -> Unit,
    onToggleCaramel: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    PatternScreenScaffold(
        modifier = modifier,
        theoryTab = { PatternTheoryTab(content = content) },
        playgroundTab = {
            when (uiState) {
                is DecoratorUiState.Idle -> PlaygroundContent(
                    uiState = uiState,
                    onToggleMilk = onToggleMilk,
                    onToggleSugar = onToggleSugar,
                    onToggleWhippedCream = onToggleWhippedCream,
                    onToggleCaramel = onToggleCaramel,
                )
            }
        },
    )
}

@Composable
private fun PlaygroundContent(
    uiState: DecoratorUiState.Idle,
    onToggleMilk: (Boolean) -> Unit,
    onToggleSugar: (Boolean) -> Unit,
    onToggleWhippedCream: (Boolean) -> Unit,
    onToggleCaramel: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Text(
            text = "Coffee Order",
            style = MaterialTheme.typography.headlineMedium,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Add Decorators",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
        )

        Spacer(modifier = Modifier.height(8.dp))

        DecoratorCheckbox("Milk (+\$0.50)", uiState.hasMilk, onToggleMilk)
        DecoratorCheckbox("Sugar (+\$0.30)", uiState.hasSugar, onToggleSugar)
        DecoratorCheckbox("Whipped Cream (+\$0.70)", uiState.hasWhippedCream, onToggleWhippedCream)
        DecoratorCheckbox("Caramel (+\$0.60)", uiState.hasCaramel, onToggleCaramel)

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ),
            shape = RoundedCornerShape(12.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Your Order",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = uiState.description,
                    style = MaterialTheme.typography.bodyLarge,
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "$%.2f".format(uiState.price),
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Decoration Chain",
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
                text = buildDecorationChain(uiState),
                style = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace),
                modifier = Modifier.padding(12.dp),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun DecoratorCheckbox(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
    }
}

private fun buildDecorationChain(state: DecoratorUiState.Idle): String {
    val layers = mutableListOf("BasicCoffee()")
    if (state.hasMilk) layers.add("MilkDecorator(...)")
    if (state.hasSugar) layers.add("SugarDecorator(...)")
    if (state.hasWhippedCream) layers.add("WhippedCreamDecorator(...)")
    if (state.hasCaramel) layers.add("CaramelDecorator(...)")
    return layers.joinToString(separator = "\n  └─ ")
}

@Preview(showBackground = true)
@Composable
private fun DecoratorScreenPreview() {
    HandyPlayTheme {
        DecoratorScreen(
            uiState = DecoratorUiState.Idle(
                hasMilk = true,
                hasCaramel = true,
                description = "Caramel Milk Basic Coffee",
                price = 3.10,
            ),
            content = PatternContent("Decorator", "Structural", "Desc", listOf("Use"), "Ex", "Code"),
            onToggleMilk = {},
            onToggleSugar = {},
            onToggleWhippedCream = {},
            onToggleCaramel = {},
        )
    }
}
