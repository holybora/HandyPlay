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

    private val sampleTopics: List<Topic> = listOf(
        Topic.DesignPattern.FactoryMethod,
        Topic.DesignPattern.Observer,
        Topic.DesignPattern.Strategy,
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

        composeTestRule.onNodeWithText("Factory Method").assertDoesNotExist()
    }

    @Test
    fun successStateDisplaysAllTopicNames() {
        composeTestRule.setContent {
            HandyPlayTheme {
                CategoryScreen(
                    uiState = CategoryUiState.Success(
                        categoryName = "Design Patterns",
                        topics = sampleTopics
                    ),
                    onTopicClick = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Factory Method").assertIsDisplayed()
        composeTestRule.onNodeWithText("Observer").assertIsDisplayed()
        composeTestRule.onNodeWithText("Strategy").assertIsDisplayed()
    }

    @Test
    fun successStateWithEmptyListShowsNoCards() {
        composeTestRule.setContent {
            HandyPlayTheme {
                CategoryScreen(
                    uiState = CategoryUiState.Success(
                        categoryName = "Design Patterns",
                        topics = emptyList()
                    ),
                    onTopicClick = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Factory Method").assertDoesNotExist()
    }

    @Test
    fun clickingTopicInvokesCallback() {
        var clickedTopic: Topic? = null
        composeTestRule.setContent {
            HandyPlayTheme {
                CategoryScreen(
                    uiState = CategoryUiState.Success(
                        categoryName = "Design Patterns",
                        topics = sampleTopics
                    ),
                    onTopicClick = { clickedTopic = it },
                )
            }
        }

        composeTestRule.onNodeWithText("Factory Method").performClick()
        assertEquals(Topic.DesignPattern.FactoryMethod, clickedTopic)
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
