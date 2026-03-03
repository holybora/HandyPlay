package com.sls.handbook.feature.dp.creational.factorymethod

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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sls.handbook.core.designsystem.theme.HandyPlayTheme
import com.sls.handbook.core.model.PatternContent
import com.sls.handbook.core.ui.PatternScreenScaffold
import com.sls.handbook.core.ui.PatternTheoryTab

@Composable
fun FactoryMethodScreen(
    uiState: FactoryMethodUiState,
    content: PatternContent,
    onTypeSelect: (NotificationType) -> Unit,
    onCreateClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PatternScreenScaffold(
        modifier = modifier,
        theoryTab = { PatternTheoryTab(content = content) },
        playgroundTab = {
            when (uiState) {
                is FactoryMethodUiState.Idle -> PlaygroundContent(
                    uiState = uiState,
                    onTypeSelect = onTypeSelect,
                    onCreateClick = onCreateClick,
                )
            }
        },
    )
}

@Composable
private fun PlaygroundContent(
    uiState: FactoryMethodUiState.Idle,
    onTypeSelect: (NotificationType) -> Unit,
    onCreateClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Text(
            text = "Notification Factory",
            style = MaterialTheme.typography.headlineMedium,
        )

        Spacer(modifier = Modifier.height(16.dp))

        NotificationTypeDropdown(
            selectedType = uiState.selectedType,
            onTypeSelect = onTypeSelect,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onCreateClick,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = "Create Notification")
        }

        Spacer(modifier = Modifier.height(16.dp))

        uiState.createdNotification?.let { notification ->
            NotificationCard(notification = notification)
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (uiState.log.isNotEmpty()) {
            LogSection(log = uiState.log)
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NotificationTypeDropdown(
    selectedType: NotificationType,
    onTypeSelect: (NotificationType) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier.fillMaxWidth(),
    ) {
        OutlinedTextField(
            value = selectedType.displayName,
            onValueChange = {},
            readOnly = true,
            label = { Text("Notification Type") },
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
            NotificationType.entries.forEach { type ->
                DropdownMenuItem(
                    text = { Text(type.displayName) },
                    onClick = {
                        onTypeSelect(type)
                        expanded = false
                    },
                )
            }
        }
    }
}

@Composable
private fun NotificationCard(
    notification: NotificationDisplay,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
        shape = RoundedCornerShape(12.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = notification.title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Channel: ${notification.channel}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Properties",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f),
            )

            Spacer(modifier = Modifier.height(4.dp))

            notification.properties.forEach { (key, value) ->
                Row(modifier = Modifier.padding(vertical = 2.dp)) {
                    Text(
                        text = "$key:",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = value,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}

@Composable
private fun LogSection(
    log: List<String>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Factory Log",
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
                text = log.joinToString(separator = "\n"),
                style = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace),
                modifier = Modifier.padding(12.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FactoryMethodScreenPreview() {
    HandyPlayTheme {
        FactoryMethodScreen(
            uiState = FactoryMethodUiState.Idle(
                createdNotification = NotificationDisplay(
                    title = "Email Notification",
                    channel = "SMTP",
                    properties = mapOf("Format" to "HTML", "Subject" to "Hello!"),
                ),
                log = listOf("EmailNotificationFactory.createNotification() called"),
            ),
            content = PatternContent("Factory Method", "Creational", "Desc", listOf("Use"), "Ex", "Code"),
            onTypeSelect = {},
            onCreateClick = {},
        )
    }
}
