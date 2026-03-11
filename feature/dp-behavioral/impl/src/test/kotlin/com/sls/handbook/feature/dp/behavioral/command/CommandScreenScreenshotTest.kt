package com.sls.handbook.feature.dp.behavioral.command

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
class CommandScreenScreenshotTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ScreenshotTestActivity>()

    @Test
    fun commandScreen_default() {
        composeTestRule.captureScreenshot("CommandScreen_Default") {
            HandyPlayTheme {
                CommandScreen(
                    uiState = CommandUiState.Idle(
                        isBold = true,
                        commandHistory = listOf("Execute: Bold"),
                        undoStack = listOf(TextCommand.ToggleBold(false)),
                    ),
                    content = PatternContent("Command", "Behavioral", "Desc", listOf("Use"), "Ex", "Code"),
                    onToggleBold = {},
                    onToggleItalic = {},
                    onToggleUnderline = {},
                    onTextChange = {},
                    onUndo = {},
                    onRedo = {},
                )
            }
        }
    }
}
