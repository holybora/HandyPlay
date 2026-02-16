package com.sls.handbook.feature.ttlcache

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.sls.handbook.core.designsystem.theme.HandyPlayTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TtlCacheScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun idleStateDisplaysTitleText() {
        composeTestRule.setContent {
            HandyPlayTheme {
                TtlCacheScreen(
                    uiState = TtlCacheUiState.Idle(),
                    onTtlChange = {},
                    onGetClick = {},
                )
            }
        }

        composeTestRule.onNodeWithText("TTL Cache Demo").assertIsDisplayed()
    }

    @Test
    fun idleStateDisplaysLastFetchedTimeLabel() {
        composeTestRule.setContent {
            HandyPlayTheme {
                TtlCacheScreen(
                    uiState = TtlCacheUiState.Idle(),
                    onTtlChange = {},
                    onGetClick = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Last fetched time").assertIsDisplayed()
    }

    @Test
    fun idleStateDisplaysDataLabel() {
        composeTestRule.setContent {
            HandyPlayTheme {
                TtlCacheScreen(
                    uiState = TtlCacheUiState.Idle(),
                    onTtlChange = {},
                    onGetClick = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Data").assertIsDisplayed()
    }

    @Test
    fun idleStateDisplaysGetButton() {
        composeTestRule.setContent {
            HandyPlayTheme {
                TtlCacheScreen(
                    uiState = TtlCacheUiState.Idle(),
                    onTtlChange = {},
                    onGetClick = {},
                )
            }
        }

        composeTestRule.onNodeWithText("GET").assertIsDisplayed()
    }

    @Test
    fun idleStateGetButtonIsEnabledWhenNotLoading() {
        composeTestRule.setContent {
            HandyPlayTheme {
                TtlCacheScreen(
                    uiState = TtlCacheUiState.Idle(isLoading = false),
                    onTtlChange = {},
                    onGetClick = {},
                )
            }
        }

        composeTestRule.onNodeWithText("GET").assertIsEnabled()
    }

    @Test
    fun idleStateWithDataDisplaysDataText() {
        val testData = "Setup: joke\nPunchline: punchline"
        composeTestRule.setContent {
            HandyPlayTheme {
                TtlCacheScreen(
                    uiState = TtlCacheUiState.Idle(data = testData),
                    onTtlChange = {},
                    onGetClick = {},
                )
            }
        }

        composeTestRule.onNodeWithText(testData, substring = true).assertIsDisplayed()
    }

    @Test
    fun idleStateWithLastFetchedTimeDisplaysTime() {
        composeTestRule.setContent {
            HandyPlayTheme {
                TtlCacheScreen(
                    uiState = TtlCacheUiState.Idle(lastFetchedTime = "14:30:25 (fresh)"),
                    onTtlChange = {},
                    onGetClick = {},
                )
            }
        }

        composeTestRule.onNodeWithText("14:30:25 (fresh)").assertIsDisplayed()
    }

    @Test
    fun loadingStateDisablesGetButton() {
        composeTestRule.setContent {
            HandyPlayTheme {
                TtlCacheScreen(
                    uiState = TtlCacheUiState.Idle(isLoading = true),
                    onTtlChange = {},
                    onGetClick = {},
                )
            }
        }

        composeTestRule.onNodeWithText("GET").assertIsNotEnabled()
    }

    @Test
    fun clickingGetButtonInvokesCallback() {
        var clicked = false
        composeTestRule.setContent {
            HandyPlayTheme {
                TtlCacheScreen(
                    uiState = TtlCacheUiState.Idle(),
                    onTtlChange = {},
                    onGetClick = { clicked = true },
                )
            }
        }

        composeTestRule.onNodeWithText("GET").performClick()
        assertTrue(clicked)
    }

    @Test
    fun errorStateDisplaysErrorMessage() {
        composeTestRule.setContent {
            HandyPlayTheme {
                TtlCacheScreen(
                    uiState = TtlCacheUiState.Error("Something went wrong"),
                    onTtlChange = {},
                    onGetClick = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Something went wrong").assertIsDisplayed()
    }

    @Test
    fun errorStateDoesNotShowGetButton() {
        composeTestRule.setContent {
            HandyPlayTheme {
                TtlCacheScreen(
                    uiState = TtlCacheUiState.Error("Something went wrong"),
                    onTtlChange = {},
                    onGetClick = {},
                )
            }
        }

        composeTestRule.onNodeWithText("GET").assertDoesNotExist()
    }
}
