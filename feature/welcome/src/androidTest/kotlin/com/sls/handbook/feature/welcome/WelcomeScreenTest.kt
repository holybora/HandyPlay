package com.sls.handbook.feature.welcome

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
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
class WelcomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun welcomeTextIsDisplayed() {
        composeTestRule.setContent {
            HandyPlayTheme {
                WelcomeScreen()
            }
        }

        composeTestRule.onNodeWithText("Welcome to HandyPlay").assertIsDisplayed()
    }

    @Test
    fun getStartedButtonIsDisplayed() {
        composeTestRule.setContent {
            HandyPlayTheme {
                WelcomeScreen()
            }
        }

        composeTestRule.onNodeWithText("Get Started").assertIsDisplayed()
    }

    @Test
    fun clickingGetStartedButtonInvokesCallback() {
        var clicked = false
        composeTestRule.setContent {
            HandyPlayTheme {
                WelcomeScreen(onStart = { clicked = true })
            }
        }

        composeTestRule.onNodeWithText("Get Started").performClick()
        assertTrue(clicked)
    }

    @Test
    fun getStartedButtonIsClickable() {
        composeTestRule.setContent {
            HandyPlayTheme {
                WelcomeScreen()
            }
        }

        composeTestRule.onNodeWithText("Get Started").assertHasClickAction()
    }
}
