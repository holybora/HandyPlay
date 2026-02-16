package com.sls.handbook.feature.category

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sls.handbook.core.designsystem.theme.HandyPlayTheme
import com.sls.handbook.core.model.Topic
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CategoryScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val sampleTopics = listOf(
        Topic(id = "1", name = "Variables & Types", categoryId = "kotlin"),
        Topic(id = "2", name = "Control Flow", categoryId = "kotlin"),
        Topic(id = "3", name = "Functions", categoryId = "kotlin"),
    )

    @Test
    fun loadingStateDoesNotShowTopicCards() {
        composeTestRule.setContent {
            HandyPlayTheme {
                CategoryScreen(
                    uiState = CategoryUiState.Loading,
                    onTopicClick = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Variables & Types").assertDoesNotExist()
    }

    @Test
    fun successStateDisplaysAllTopicNames() {
        composeTestRule.setContent {
            HandyPlayTheme {
                CategoryScreen(
                    uiState = CategoryUiState.Success(
                        categoryName = "Kotlin",
                        topics = sampleTopics
                    ),
                    onTopicClick = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Variables & Types").assertIsDisplayed()
        composeTestRule.onNodeWithText("Control Flow").assertIsDisplayed()
        composeTestRule.onNodeWithText("Functions").assertIsDisplayed()
    }

    @Test
    fun successStateWithEmptyListShowsNoCards() {
        composeTestRule.setContent {
            HandyPlayTheme {
                CategoryScreen(
                    uiState = CategoryUiState.Success(
                        categoryName = "Kotlin",
                        topics = emptyList()
                    ),
                    onTopicClick = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Variables & Types").assertDoesNotExist()
    }

    @Test
    fun clickingTopicInvokesCallback() {
        var clickedTopicId: String? = null
        composeTestRule.setContent {
            HandyPlayTheme {
                CategoryScreen(
                    uiState = CategoryUiState.Success(
                        categoryName = "Kotlin",
                        topics = sampleTopics
                    ),
                    onTopicClick = { clickedTopicId = it },
                )
            }
        }

        composeTestRule.onNodeWithText("Variables & Types").performClick()
        assertEquals("1", clickedTopicId)
    }

    @Test
    fun errorStateDisplaysErrorMessage() {
        composeTestRule.setContent {
            HandyPlayTheme {
                CategoryScreen(
                    uiState = CategoryUiState.Error(message = "Failed to load topics"),
                    onTopicClick = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Failed to load topics").assertIsDisplayed()
    }

}
