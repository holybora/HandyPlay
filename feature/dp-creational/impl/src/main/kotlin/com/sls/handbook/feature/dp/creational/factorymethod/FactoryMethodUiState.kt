package com.sls.handbook.feature.dp.creational.factorymethod

sealed interface FactoryMethodUiState {
    data class Idle(
        val selectedType: NotificationType = NotificationType.EMAIL,
        val createdNotification: NotificationDisplay? = null,
        val log: List<String> = emptyList(),
    ) : FactoryMethodUiState
}

enum class NotificationType(val displayName: String) {
    EMAIL("Email"),
    SMS("SMS"),
    PUSH("Push Notification"),
}

data class NotificationDisplay(
    val title: String,
    val channel: String,
    val properties: Map<String, String>,
)
