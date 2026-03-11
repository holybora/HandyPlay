package com.sls.handbook.feature.dp.structural.decorator

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
class DecoratorScreenScreenshotTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ScreenshotTestActivity>()

    @Test
    fun decoratorScreen_default() {
        composeTestRule.captureScreenshot("DecoratorScreen_Default") {
            HandyPlayTheme {
                DecoratorScreen(
                    uiState = DecoratorUiState.Idle(
                        hasMilk = true,
                        hasCaramel = true,
                        description = "Caramel Milk Basic Coffee",
                        price = 3.10,
                    ),
                    content = PatternContent("Decorator", "Structural", "Desc", listOf("Use"), "Ex", "Code"),
                    onToggleMilk = {},
                    onToggleSugar = {},
                    onToggleWhippedCream = {},
                    onToggleCaramel = {},
                )
            }
        }
    }
}
