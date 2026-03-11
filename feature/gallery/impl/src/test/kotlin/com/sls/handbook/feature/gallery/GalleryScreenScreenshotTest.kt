package com.sls.handbook.feature.gallery

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.sls.handbook.core.screenshottesting.ScreenshotTestActivity
import com.sls.handbook.core.designsystem.theme.HandyPlayTheme
import com.sls.handbook.core.model.GalleryImage
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
class GalleryScreenScreenshotTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ScreenshotTestActivity>()

    @Test
    fun galleryScreen_loading() {
        composeTestRule.captureScreenshot("GalleryScreen_Loading") {
            HandyPlayTheme {
                GalleryScreen(
                    uiState = GalleryUiState.Loading,
                    onLoadMore = {},
                    onRetry = {},
                )
            }
        }
    }

    @Test
    fun galleryScreen_error() {
        composeTestRule.captureScreenshot("GalleryScreen_Error") {
            HandyPlayTheme {
                GalleryScreen(
                    uiState = GalleryUiState.Error(message = "Network error"),
                    onLoadMore = {},
                    onRetry = {},
                )
            }
        }
    }

    @Test
    fun galleryScreen_content() {
        composeTestRule.captureScreenshot("GalleryScreen_Content") {
            HandyPlayTheme {
                GalleryScreen(
                    uiState = GalleryUiState.Content(
                        images = listOf(
                            GalleryImage("1", "Author One", 300, 300, "https://picsum.photos/id/1/300"),
                            GalleryImage("2", "Author Two", 300, 300, "https://picsum.photos/id/2/300"),
                        ),
                    ),
                    onLoadMore = {},
                    onRetry = {},
                )
            }
        }
    }
}
