package com.sls.handbook.feature.ttlcache

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sls.handbook.core.designsystem.theme.HandyPlayTheme

@Composable
fun TtlCacheScreen(
    uiState: TtlCacheUiState,
    onTtlChange: (Int) -> Unit,
    onGetClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        is TtlCacheUiState.Idle -> TtlCacheIdleContent(
            uiState = uiState,
            onTtlChange = onTtlChange,
            onGetClick = onGetClick,
            modifier = modifier,
        )

        is TtlCacheUiState.Error -> Text(
            text = uiState.message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error,
            modifier = modifier.padding(16.dp),
        )
    }
}

@Composable
private fun TtlCacheIdleContent(
    uiState: TtlCacheUiState.Idle,
    onTtlChange: (Int) -> Unit,
    onGetClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Text(
            text = "TTL Cache Demo",
            style = MaterialTheme.typography.headlineMedium,
        )

        Spacer(modifier = Modifier.height(16.dp))

        TtlDropdown(
            selectedSeconds = uiState.ttlSeconds,
            onTtlChange = onTtlChange,
        )

        Spacer(modifier = Modifier.height(12.dp))

        LabeledText(
            label = "Last fetched time",
            text = uiState.lastFetchedTime,
        )

        Spacer(modifier = Modifier.height(12.dp))

        CodeDisplay(
            label = "Data",
            text = uiState.data,
        )

        Spacer(modifier = Modifier.height(16.dp))

        AnimatedVisibility(
            visible = uiState.isLoading,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onGetClick,
            enabled = !uiState.isLoading,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = "GET")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TtlDropdown(
    selectedSeconds: Int,
    onTtlChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    val ttlOptions = listOf(1, 2, 3, 4, 5)

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier.fillMaxWidth(),
    ) {
        OutlinedTextField(
            value = "$selectedSeconds seconds",
            onValueChange = {},
            readOnly = true,
            label = { Text("TTL time") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            ttlOptions.forEach { option ->
                DropdownMenuItem(
                    text = { Text("$option seconds") },
                    onClick = {
                        onTtlChange(option)
                        expanded = false
                    },
                )
            }
        }
    }
}

@Composable
private fun LabeledText(
    label: String,
    text: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = text.ifEmpty { "-" },
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
private fun CodeDisplay(
    label: String,
    text: String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
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
                text = text.ifEmpty { "-" },
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = FontFamily.Monospace,
                ),
                modifier = Modifier.padding(12.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TtlCacheScreenIdlePreview() {
    HandyPlayTheme {
        TtlCacheScreen(
            uiState = TtlCacheUiState.Idle(),
            onTtlChange = {},
            onGetClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TtlCacheScreenLoadingPreview() {
    HandyPlayTheme {
        TtlCacheScreen(
            uiState = TtlCacheUiState.Idle(isLoading = true),
            onTtlChange = {},
            onGetClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TtlCacheScreenWithDataPreview() {
    HandyPlayTheme {
        TtlCacheScreen(
            uiState = TtlCacheUiState.Idle(
                lastFetchedTime = "14:30:25 (fresh)",
                data = "Setup: Why don't scientists trust atoms?\n\nPunchline: Because they make up everything!",
            ),
            onTtlChange = {},
            onGetClick = {},
        )
    }
}
