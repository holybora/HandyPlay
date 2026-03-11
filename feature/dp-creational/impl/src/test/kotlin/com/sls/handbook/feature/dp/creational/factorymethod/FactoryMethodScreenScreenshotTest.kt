package com.sls.handbook.feature.dp.creational.factorymethod

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
class FactoryMethodScreenScreenshotTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ScreenshotTestActivity>()

    @Test
    fun factoryMethodScreen_default() {
        composeTestRule.captureScreenshot("FactoryMethodScreen_Default") {
            HandyPlayTheme {
                FactoryMethodScreen(
                    uiState = FactoryMethodUiState.Idle(
                        createdNotification = NotificationDisplay(
                            title = "Email Notification",
                            channel = "SMTP",
                            properties = mapOf("Format" to "HTML", "Subject" to "Hello!"),
                        ),
                        log = listOf("EmailNotificationFactory.createNotification() called"),
                    ),
                    content = PatternContent("Factory Method", "Creational", "Desc", listOf("Use"), "Ex", "Code"),
                    onTypeSelect = {},
                    onCreateClick = {},
                )
            }
        }
    }
}
