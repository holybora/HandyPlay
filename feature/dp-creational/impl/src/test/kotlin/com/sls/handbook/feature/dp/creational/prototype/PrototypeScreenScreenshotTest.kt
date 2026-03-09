package com.sls.handbook.feature.dp.creational.prototype

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
class PrototypeScreenScreenshotTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ScreenshotTestActivity>()

    @Test
    fun prototypeScreen_default() {
        composeTestRule.captureScreenshot("PrototypeScreen_Default") {
            HandyPlayTheme {
                PrototypeScreen(
                    uiState = PrototypeUiState.Idle(
                        clones = listOf(
                            ShapePrototype(ShapeType.CIRCLE, "Red", 0xFFF44336, 80),
                            ShapePrototype(ShapeType.CIRCLE, "Blue", 0xFF2196F3, 120),
                        ),
                        selectedCloneIndex = 0,
                    ),
                    content = PatternContent("Prototype", "Creational", "Desc", listOf("Use"), "Ex", "Code"),
                    onClone = {},
                    onSelectClone = {},
                    onCloneColorChange = { _, _, _ -> },
                    onCloneSizeChange = { _, _ -> },
                    onOriginalTypeChange = {},
                )
            }
        }
    }
}
