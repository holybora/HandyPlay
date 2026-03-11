package com.sls.handbook.feature.dp.behavioral.statemachine

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.sls.handbook.core.screenshottesting.ScreenshotTestActivity
import com.sls.handbook.core.designsystem.theme.HandyPlayTheme
import com.sls.handbook.core.model.PatternContent
import com.sls.handbook.core.screenshottesting.captureScreenshot
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [34])
class StateMachineScreenScreenshotTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ScreenshotTestActivity>()

    @Test
    fun stateMachineScreen_default() {
        composeTestRule.captureScreenshot("StateMachineScreen_Default") {
            HandyPlayTheme {
                StateMachineScreen(
                    uiState = StateMachineUiState.Idle(
                        currentState = VendingState.HAS_COIN,
                        coins = 1,
                        log = listOf("Machine ready.", "Coin inserted."),
                    ),
                    content = PatternContent("State Machine", "Behavioral", "Desc", listOf("Use"), "Ex", "Code"),
                    onInsertCoin = {},
                    onSelectItem = {},
                    onDispense = {},
                    onReset = {},
                )
            }
        }
    }
}
