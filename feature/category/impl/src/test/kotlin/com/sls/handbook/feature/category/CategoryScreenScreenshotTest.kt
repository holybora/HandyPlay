package com.sls.handbook.feature.category

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.sls.handbook.core.screenshottesting.ScreenshotTestActivity
import com.sls.handbook.core.designsystem.theme.HandyPlayTheme
import com.sls.handbook.core.model.Topic
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
class CategoryScreenScreenshotTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ScreenshotTestActivity>()

    private val sampleTopics: List<Topic> = listOf(
        Topic.KotlinFundamental.TtlCache,
        Topic.Ui.Gallery,
        Topic.Ui.Fever,
        Topic.DesignPattern.FactoryMethod,
        Topic.DesignPattern.Observer,
    )

    @Test
    fun categoryScreen_light() {
        composeTestRule.captureScreenshot("CategoryScreen_Light") {
            HandyPlayTheme(darkTheme = false) {
                CategoryScreen(
                    uiState = CategoryUiState.Success(
                        categoryName = "Kotlin Fundamentals",
                        topics = sampleTopics,
                    ),
                    onTopicClick = {},
                )
            }
        }
    }

    @Test
    fun categoryScreen_dark() {
        composeTestRule.captureScreenshot("CategoryScreen_Dark") {
            HandyPlayTheme(darkTheme = true) {
                CategoryScreen(
                    uiState = CategoryUiState.Success(
                        categoryName = "Kotlin Fundamentals",
                        topics = sampleTopics,
                    ),
                    onTopicClick = {},
                )
            }
        }
    }

    @Test
    fun categoryScreen_loading() {
        composeTestRule.captureScreenshot("CategoryScreen_Loading") {
            HandyPlayTheme {
                CategoryScreen(
                    uiState = CategoryUiState.Loading,
                    onTopicClick = {},
                )
            }
        }
    }

    @Test
    fun categoryScreen_empty() {
        composeTestRule.captureScreenshot("CategoryScreen_Empty") {
            HandyPlayTheme {
                CategoryScreen(
                    uiState = CategoryUiState.Success(
                        categoryName = "Kotlin Fundamentals",
                        topics = emptyList(),
                        searchQuery = "xyz",
                    ),
                    onTopicClick = {},
                )
            }
        }
    }
}
