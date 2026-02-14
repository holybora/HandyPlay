package com.sls.handbook.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.sls.handbook.core.designsystem.theme.HandyPlayTheme
import com.sls.handbook.feature.category.CategoryRoute
import com.sls.handbook.feature.home.HomeRoute
import com.sls.handbook.feature.topic.TopicRoute
import com.sls.handbook.feature.welcome.WelcomeRoute
import com.sls.handbook.navigation.CategoryDestination
import com.sls.handbook.navigation.HomeDestination
import com.sls.handbook.navigation.TopicDestination
import com.sls.handbook.navigation.WelcomeDestination

@Composable
fun HandyPlayApp(modifier: Modifier = Modifier) {
    HandyPlayTheme {
        val navController = rememberNavController()

        Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = WelcomeDestination,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable<WelcomeDestination> {
                    WelcomeRoute(
                        onStart = {
                            navController.navigate(HomeDestination)
                        }
                    )
                }

                composable<HomeDestination> {
                    HomeRoute(
                        onCategoryClick = { category ->
                            navController.navigate(
                                CategoryDestination(
                                    categoryId = category.id,
                                    categoryName = category.name,
                                )
                            )
                        }
                    )
                }

                composable<CategoryDestination> { backStackEntry ->
                    val categoryDest = backStackEntry.toRoute<CategoryDestination>()
                    CategoryRoute(
                        onTopicClick = { topic ->
                            navController.navigate(
                                TopicDestination(
                                    topicId = topic.id,
                                    topicName = topic.name,
                                    categoryId = categoryDest.categoryId,
                                    categoryName = categoryDest.categoryName,
                                )
                            )
                        },
                    )
                }

                composable<TopicDestination> {
                    TopicRoute()
                }
            }
        }
    }
}
