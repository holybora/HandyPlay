package com.sls.handbook.feature.dp.structural.facade

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
class FacadeScreenScreenshotTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ScreenshotTestActivity>()

    @Test
    fun facadeScreen_default() {
        composeTestRule.captureScreenshot("FacadeScreen_Default") {
            HandyPlayTheme {
                FacadeScreen(
                    uiState = FacadeUiState.Idle(
                        projectorOn = true,
                        soundSurround = true,
                        lightsDimmed = true,
                        log = listOf("Facade: watchMovie() called"),
                    ),
                    content = PatternContent("Facade", "Structural", "Desc", listOf("Use"), "Ex", "Code"),
                    onToggleFacade = {},
                    onWatchMovie = {},
                    onStopMovie = {},
                    onToggleProjector = {},
                    onToggleSound = {},
                    onToggleLights = {},
                )
            }
        }
    }
}
