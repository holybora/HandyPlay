package com.sls.handbook.feature.welcome

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.sls.handbook.core.screenshottesting.ScreenshotTestActivity
import com.sls.handbook.core.designsystem.theme.HandyPlayTheme
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
class WelcomeScreenScreenshotTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ScreenshotTestActivity>()

    @Test
    fun welcomeScreen_light() {
        composeTestRule.captureScreenshot("WelcomeScreen_Light") {
            HandyPlayTheme(darkTheme = false) {
                WelcomeScreen()
            }
        }
    }

    @Test
    fun welcomeScreen_dark() {
        composeTestRule.captureScreenshot("WelcomeScreen_Dark") {
            HandyPlayTheme(darkTheme = true) {
                WelcomeScreen()
            }
        }
    }
}
