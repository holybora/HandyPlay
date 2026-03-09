package com.sls.handbook.feature.dp.creational.abstractfactory

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
class AbstractFactoryScreenScreenshotTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ScreenshotTestActivity>()

    @Test
    fun abstractFactoryScreen_default() {
        composeTestRule.captureScreenshot("AbstractFactoryScreen_Default") {
            HandyPlayTheme {
                AbstractFactoryScreen(
                    uiState = AbstractFactoryUiState.Idle(),
                    content = PatternContent("Abstract Factory", "Creational", "Desc", listOf("Use"), "Ex", "Code"),
                    onThemeSelect = {},
                )
            }
        }
    }
}
