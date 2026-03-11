package com.sls.handbook.feature.dp.behavioral.strategy

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
class StrategyScreenScreenshotTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ScreenshotTestActivity>()

    @Test
    fun strategyScreen_default() {
        composeTestRule.captureScreenshot("StrategyScreen_Default") {
            HandyPlayTheme {
                StrategyScreen(
                    uiState = StrategyUiState.Idle(
                        sortedNumbers = listOf(3, 9, 10, 27, 38, 43, 82),
                        stepCount = 12,
                        comparisonCount = 21,
                    ),
                    content = PatternContent("Strategy", "Behavioral", "Desc", listOf("Use"), "Ex", "Code"),
                    onStrategySelect = {},
                    onSort = {},
                    onShuffle = {},
                )
            }
        }
    }
}
