package com.sls.handbook.feature.dp.creational.factorymethod

import androidx.lifecycle.ViewModel
import com.sls.handbook.core.model.PatternContent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class FactoryMethodViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<FactoryMethodUiState>(FactoryMethodUiState.Idle())
    val uiState: StateFlow<FactoryMethodUiState> = _uiState.asStateFlow()

    val content = PatternContent(
        title = "Factory Method",
        subtitle = "Creational Pattern",
        description = "Factory Method defines an interface for creating an object, but lets subclasses " +
            "decide which class to instantiate. It lets a class defer instantiation to subclasses, " +
            "promoting loose coupling between the creator and the concrete products.",
        whenToUse = listOf(
            "When a class can't anticipate the type of objects it needs to create",
            "When a class wants its subclasses to specify the objects it creates",
            "When you want to localize the knowledge of which helper subclass is the delegate",
            "When you need to decouple object creation from usage",
        ),
        androidExamples = "ViewModelProvider.Factory is the classic Android example — it defines a " +
            "create() method that different factory implementations override to produce different " +
            "ViewModel types. Other examples include Fragment.instantiate(), Intent creation " +
            "helpers, and custom View factories.",
        structure = """interface Creator {
    fun factoryMethod(): Product
}

class ConcreteCreatorA : Creator {
    override fun factoryMethod(): Product =
        ConcreteProductA()
}

class ConcreteCreatorB : Creator {
    override fun factoryMethod(): Product =
        ConcreteProductB()
}

interface Product {
    fun use()
}""",
    )

    fun onTypeSelected(type: NotificationType) {
        _uiState.update { currentState ->
            if (currentState is FactoryMethodUiState.Idle) {
                currentState.copy(selectedType = type)
            } else {
                currentState
            }
        }
    }

    fun onCreateClick() {
        val currentState = _uiState.value
        if (currentState !is FactoryMethodUiState.Idle) return

        val factory = getFactory(currentState.selectedType)
        val notification = factory.createNotification("Hello from Factory Method!")
        val logEntry = "${factory.name}.createNotification() called"

        _uiState.value = currentState.copy(
            createdNotification = notification,
            log = currentState.log + logEntry,
        )
    }

    private fun getFactory(type: NotificationType): NotificationFactory = when (type) {
        NotificationType.EMAIL -> EmailNotificationFactory()
        NotificationType.SMS -> SmsNotificationFactory()
        NotificationType.PUSH -> PushNotificationFactory()
    }

    private interface NotificationFactory {
        val name: String
        fun createNotification(message: String): NotificationDisplay
    }

    private class EmailNotificationFactory : NotificationFactory {
        override val name = "EmailNotificationFactory"
        override fun createNotification(message: String) = NotificationDisplay(
            title = "Email Notification",
            channel = "SMTP",
            properties = mapOf(
                "Format" to "HTML",
                "Subject" to message,
                "Sender" to "noreply@example.com",
                "Priority" to "Normal",
            ),
        )
    }

    private class SmsNotificationFactory : NotificationFactory {
        override val name = "SmsNotificationFactory"
        override fun createNotification(message: String) = NotificationDisplay(
            title = "SMS Notification",
            channel = "Cellular",
            properties = mapOf(
                "Format" to "Plain Text",
                "Body" to message,
                "Max Length" to "160 chars",
                "Encoding" to "GSM-7",
            ),
        )
    }

    private class PushNotificationFactory : NotificationFactory {
        override val name = "PushNotificationFactory"
        override fun createNotification(message: String) = NotificationDisplay(
            title = "Push Notification",
            channel = "FCM",
            properties = mapOf(
                "Format" to "JSON Payload",
                "Body" to message,
                "Sound" to "default",
                "Badge" to "1",
            ),
        )
    }
}
