package com.sls.handbook.feature.home

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.sls.handbook.core.screenshottesting.ScreenshotTestActivity
import com.sls.handbook.core.designsystem.theme.HandyPlayTheme
import com.sls.handbook.core.model.Category
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
class HomeScreenScreenshotTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ScreenshotTestActivity>()

    private val sampleCategories = listOf(
        Category(id = "1", name = "Kotlin Fundamentals"),
        Category(id = "2", name = "Android Core"),
        Category(id = "3", name = "Jetpack Compose"),
        Category(id = "4", name = "Architecture"),
        Category(id = "5", name = "Testing"),
        Category(id = "6", name = "Performance"),
    )

    @Test
    fun homeScreen_light() {
        composeTestRule.captureScreenshot("HomeScreen_Light") {
            HandyPlayTheme(darkTheme = false) {
                HomeScreen(
                    uiState = HomeUiState.Success(categories = sampleCategories),
                    onCategoryClick = {},
                )
            }
        }
    }

    @Test
    fun homeScreen_dark() {
        composeTestRule.captureScreenshot("HomeScreen_Dark") {
            HandyPlayTheme(darkTheme = true) {
                HomeScreen(
                    uiState = HomeUiState.Success(categories = sampleCategories),
                    onCategoryClick = {},
                )
            }
        }
    }

    @Test
    fun homeScreen_loading() {
        composeTestRule.captureScreenshot("HomeScreen_Loading") {
            HandyPlayTheme {
                HomeScreen(
                    uiState = HomeUiState.Loading,
                    onCategoryClick = {},
                )
            }
        }
    }

    @Test
    fun homeScreen_empty() {
        composeTestRule.captureScreenshot("HomeScreen_Empty") {
            HandyPlayTheme {
                HomeScreen(
                    uiState = HomeUiState.Success(categories = emptyList(), searchQuery = "xyz"),
                    onCategoryClick = {},
                )
            }
        }
    }
}
