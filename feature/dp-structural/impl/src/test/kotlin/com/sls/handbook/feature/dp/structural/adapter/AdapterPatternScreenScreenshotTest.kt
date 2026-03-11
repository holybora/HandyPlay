package com.sls.handbook.feature.dp.structural.adapter

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
class AdapterPatternScreenScreenshotTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ScreenshotTestActivity>()

    @Test
    fun adapterPatternScreen_default() {
        composeTestRule.captureScreenshot("AdapterPatternScreen_Default") {
            HandyPlayTheme {
                AdapterPatternScreen(
                    uiState = AdapterPatternUiState.Idle(),
                    content = PatternContent("Adapter", "Structural", "Desc", listOf("Use"), "Ex", "Code"),
                    onSystemToggle = {},
                    onTemperatureChange = {},
                    onDistanceChange = {},
                )
            }
        }
    }
}
