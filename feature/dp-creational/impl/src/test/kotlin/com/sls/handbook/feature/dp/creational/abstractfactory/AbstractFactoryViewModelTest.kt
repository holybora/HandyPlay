package com.sls.handbook.feature.dp.creational.abstractfactory

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AbstractFactoryViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: AbstractFactoryViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = AbstractFactoryViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state uses Material theme`() = runTest {
        viewModel.uiState.test {
            val state = awaitItem() as AbstractFactoryUiState.Idle
            assertEquals(ThemeFamily.MATERIAL, state.selectedTheme)
            assertEquals("#6750A4", state.button.colorHex)
        }
    }

    @Test
    fun `switching to iOS updates all components`() = runTest {
        viewModel.uiState.test {
            awaitItem()
            viewModel.onThemeSelected(ThemeFamily.IOS)
            val state = awaitItem() as AbstractFactoryUiState.Idle
            assertEquals(ThemeFamily.IOS, state.selectedTheme)
            assertEquals("#007AFF", state.button.colorHex)
            assertEquals("#8E8E93", state.textField.colorHex)
            assertEquals("#F2F2F7", state.card.colorHex)
        }
    }

    @Test
    fun `switching back to Material restores Material components`() = runTest {
        viewModel.uiState.test {
            awaitItem()
            viewModel.onThemeSelected(ThemeFamily.IOS)
            awaitItem()
            viewModel.onThemeSelected(ThemeFamily.MATERIAL)
            val state = awaitItem() as AbstractFactoryUiState.Idle
            assertEquals(ThemeFamily.MATERIAL, state.selectedTheme)
            assertEquals("#6750A4", state.button.colorHex)
        }
    }

    @Test
    fun `each theme produces coordinated component descriptions`() = runTest {
        viewModel.uiState.test {
            val materialState = awaitItem() as AbstractFactoryUiState.Idle
            assertEquals("Rounded corners, elevation shadow, ripple effect", materialState.button.description)

            viewModel.onThemeSelected(ThemeFamily.IOS)
            val iosState = awaitItem() as AbstractFactoryUiState.Idle
            assertEquals("Pill shape, no elevation, highlight on press", iosState.button.description)
        }
    }
}
