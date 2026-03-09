package com.sls.handbook.feature.ttlcache

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
class TtlCacheScreenScreenshotTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ScreenshotTestActivity>()

    @Test
    fun ttlCacheScreen_idle() {
        composeTestRule.captureScreenshot("TtlCacheScreen_Idle") {
            HandyPlayTheme {
                TtlCacheScreen(
                    uiState = TtlCacheUiState.Idle(),
                    onTtlChange = {},
                    onGetClick = {},
                )
            }
        }
    }

    @Test
    fun ttlCacheScreen_loading() {
        composeTestRule.captureScreenshot("TtlCacheScreen_Loading") {
            HandyPlayTheme {
                TtlCacheScreen(
                    uiState = TtlCacheUiState.Idle(isLoading = true),
                    onTtlChange = {},
                    onGetClick = {},
                )
            }
        }
    }

    @Test
    fun ttlCacheScreen_withData() {
        composeTestRule.captureScreenshot("TtlCacheScreen_WithData") {
            HandyPlayTheme {
                TtlCacheScreen(
                    uiState = TtlCacheUiState.Idle(
                        lastFetchedTime = "14:30:25 (fresh)",
                        data = "Setup: Why don't scientists trust atoms?\n\n" +
                            "Punchline: Because they make up everything!",
                    ),
                    onTtlChange = {},
                    onGetClick = {},
                )
            }
        }
    }
}
