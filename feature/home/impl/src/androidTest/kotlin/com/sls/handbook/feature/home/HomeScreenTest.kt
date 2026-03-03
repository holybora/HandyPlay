package com.sls.handbook.feature.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sls.handbook.core.designsystem.theme.HandyPlayTheme
import com.sls.handbook.core.model.Category
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val sampleCategories = listOf(
        Category(id = "1", name = "Kotlin Fundamentals"),
        Category(id = "2", name = "Android Core"),
        Category(id = "3", name = "Jetpack Compose"),
    )

    @Test
    fun loadingStateDoesNotShowCategoryCards() {
        composeTestRule.setContent {
            HandyPlayTheme {
                HomeScreen(
                    uiState = HomeUiState.Loading,
                    onCategoryClick = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Kotlin Fundamentals").assertDoesNotExist()
    }

    @Test
    fun successStateDisplaysAllCategoryNames() {
        composeTestRule.setContent {
            HandyPlayTheme {
                HomeScreen(
                    uiState = HomeUiState.Success(categories = sampleCategories),
                    onCategoryClick = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Kotlin Fundamentals").assertIsDisplayed()
        composeTestRule.onNodeWithText("Android Core").assertIsDisplayed()
        composeTestRule.onNodeWithText("Jetpack Compose").assertIsDisplayed()
    }

    @Test
    fun successStateWithEmptyListShowsNoCards() {
        composeTestRule.setContent {
            HandyPlayTheme {
                HomeScreen(
                    uiState = HomeUiState.Success(categories = emptyList()),
                    onCategoryClick = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Kotlin Fundamentals").assertDoesNotExist()
    }

    @Test
    fun clickingCategoryInvokesCallback() {
        var clickedCategory: Category? = null
        composeTestRule.setContent {
            HandyPlayTheme {
                HomeScreen(
                    uiState = HomeUiState.Success(categories = sampleCategories),
                    onCategoryClick = { clickedCategory = it },
                )
            }
        }

        composeTestRule.onNodeWithText("Kotlin Fundamentals").performClick()
        assertEquals(sampleCategories[0], clickedCategory)
    }

    @Test
    fun errorStateDisplaysErrorMessage() {
        composeTestRule.setContent {
            HandyPlayTheme {
                HomeScreen(
                    uiState = HomeUiState.Error(message = "Network error"),
                    onCategoryClick = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Network error").assertIsDisplayed()
    }

}
