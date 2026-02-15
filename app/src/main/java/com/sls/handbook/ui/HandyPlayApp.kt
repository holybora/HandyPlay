package com.sls.handbook.ui

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sls.handbook.core.designsystem.theme.HandyPlayTheme
import com.sls.handbook.core.model.Topic
import com.sls.handbook.feature.category.CategoryRoute
import com.sls.handbook.feature.home.HomeRoute
import com.sls.handbook.feature.ttlcache.TtlCacheRoute
import com.sls.handbook.feature.welcome.WelcomeRoute
import com.sls.handbook.navigation.CategoryDestination
import com.sls.handbook.navigation.HomeDestination
import com.sls.handbook.navigation.TtlCacheDestination
import com.sls.handbook.navigation.WelcomeDestination

@Composable
fun HandyPlayApp(modifier: Modifier = Modifier) {
    HandyPlayTheme {
        val navController = rememberNavController()

        Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = WelcomeDestination,
                modifier = Modifier.padding(innerPadding),
                enterTransition = { fadeIn(animationSpec = tween(300)) },
                exitTransition = { fadeOut(animationSpec = tween(300)) },
                popEnterTransition = { fadeIn(animationSpec = tween(300)) },
                popExitTransition = { fadeOut(animationSpec = tween(300)) },
            ) {
                composable<WelcomeDestination> {
                    WelcomeRoute(
                        onStart = {
                            navController.navigate(HomeDestination) {
                                popUpTo<WelcomeDestination> { inclusive = true }
                            }
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
                        },
                    )
                }

                composable<CategoryDestination> {
                    CategoryRoute(
                        onTopicClick = { topicId ->
                            when (topicId) {
                                Topic.ID_TTL_CACHE -> navController.navigate(TtlCacheDestination)
                            }
                        },
                        onBreadcrumbClick = { index ->
                            when (index) {
                                0 -> navController.popBackStack(
                                    route = HomeDestination,
                                    inclusive = false,
                                )
                            }
                        },
                    )
                }

                composable<TtlCacheDestination> {
                    TtlCacheRoute()
                }
            }
        }
    }
}
