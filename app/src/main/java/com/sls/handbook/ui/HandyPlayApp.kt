package com.sls.handbook.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sls.handbook.core.designsystem.theme.HandyPlayTheme
import com.sls.handbook.feature.home.HomeRoute
import com.sls.handbook.feature.welcome.WelcomeRoute
import com.sls.handbook.navigation.HomeDestination
import com.sls.handbook.navigation.WelcomeDestination

@Composable
fun HandyPlayApp() {
    HandyPlayTheme {
        val navController = rememberNavController()

        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = WelcomeDestination,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable<WelcomeDestination> {
                    WelcomeRoute(
                        onGetStarted = {
                            navController.navigate(HomeDestination)
                        }
                    )
                }

                composable<HomeDestination> {
                    HomeRoute(
                        onCategoryClick = { /* TODO: navigate to category detail */ }
                    )
                }
            }
        }
    }
}
