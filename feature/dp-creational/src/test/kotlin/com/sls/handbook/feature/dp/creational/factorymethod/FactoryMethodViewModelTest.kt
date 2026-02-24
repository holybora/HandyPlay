package com.sls.handbook.feature.dp.creational.factorymethod

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FactoryMethodViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: FactoryMethodViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = FactoryMethodViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Idle with EMAIL selected`() = runTest {
        viewModel.uiState.test {
            val state = awaitItem() as FactoryMethodUiState.Idle
            assertEquals(NotificationType.EMAIL, state.selectedType)
            assertNull(state.createdNotification)
            assertTrue(state.log.isEmpty())
        }
    }

    @Test
    fun `onTypeSelected updates selected type`() = runTest {
        viewModel.uiState.test {
            awaitItem()
            viewModel.onTypeSelected(NotificationType.SMS)
            val updated = awaitItem() as FactoryMethodUiState.Idle
            assertEquals(NotificationType.SMS, updated.selectedType)
        }
    }

    @Test
    fun `onCreateClick creates email notification`() = runTest {
        viewModel.uiState.test {
            awaitItem()
            viewModel.onCreateClick()
            val state = awaitItem() as FactoryMethodUiState.Idle
            assertEquals("Email Notification", state.createdNotification?.title)
            assertEquals("SMTP", state.createdNotification?.channel)
            assertTrue(state.log.last().contains("EmailNotificationFactory"))
        }
    }

    @Test
    fun `onCreateClick creates SMS notification`() = runTest {
        viewModel.uiState.test {
            awaitItem()
            viewModel.onTypeSelected(NotificationType.SMS)
            awaitItem()
            viewModel.onCreateClick()
            val state = awaitItem() as FactoryMethodUiState.Idle
            assertEquals("SMS Notification", state.createdNotification?.title)
            assertEquals("Cellular", state.createdNotification?.channel)
            assertTrue(state.log.last().contains("SmsNotificationFactory"))
        }
    }

    @Test
    fun `onCreateClick creates push notification`() = runTest {
        viewModel.uiState.test {
            awaitItem()
            viewModel.onTypeSelected(NotificationType.PUSH)
            awaitItem()
            viewModel.onCreateClick()
            val state = awaitItem() as FactoryMethodUiState.Idle
            assertEquals("Push Notification", state.createdNotification?.title)
            assertEquals("FCM", state.createdNotification?.channel)
            assertTrue(state.log.last().contains("PushNotificationFactory"))
        }
    }

    @Test
    fun `multiple creates accumulate log entries`() = runTest {
        viewModel.uiState.test {
            awaitItem()
            viewModel.onCreateClick()
            awaitItem()
            viewModel.onTypeSelected(NotificationType.SMS)
            awaitItem()
            viewModel.onCreateClick()
            val state = awaitItem() as FactoryMethodUiState.Idle
            assertEquals(2, state.log.size)
        }
    }
}
