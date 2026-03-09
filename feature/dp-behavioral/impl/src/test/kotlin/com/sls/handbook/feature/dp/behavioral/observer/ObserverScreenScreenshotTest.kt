package com.sls.handbook.feature.dp.behavioral.observer

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
class ObserverScreenScreenshotTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ScreenshotTestActivity>()

    @Test
    fun observerScreen_default() {
        composeTestRule.captureScreenshot("ObserverScreen_Default") {
            HandyPlayTheme {
                ObserverScreen(
                    uiState = ObserverUiState.Idle(),
                    content = PatternContent("Observer", "Behavioral", "Desc", listOf("Use"), "Ex", "Code"),
                    onPriceChange = {},
                    onTogglePortfolio = {},
                    onToggleAlert = {},
                    onToggleChart = {},
                )
            }
        }
    }
}
