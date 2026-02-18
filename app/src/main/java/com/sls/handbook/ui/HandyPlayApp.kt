package com.sls.handbook.ui

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.End
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Start
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.sls.handbook.core.designsystem.theme.HandyPlayTheme
import com.sls.handbook.core.model.Topic
import com.sls.handbook.core.ui.BottomSearchBar
import com.sls.handbook.feature.category.CategoryRoute
import com.sls.handbook.feature.gallery.GalleryRoute
import com.sls.handbook.feature.home.HomeRoute
import com.sls.handbook.feature.ttlcache.TtlCacheRoute
import com.sls.handbook.feature.welcome.WelcomeRoute
import com.sls.handbook.navigation.CategoryDestination
import com.sls.handbook.navigation.GalleryDestination
import com.sls.handbook.navigation.HomeDestination
import com.sls.handbook.navigation.TtlCacheDestination
import com.sls.handbook.navigation.WelcomeDestination
import com.theapache64.rebugger.Rebugger

@Suppress("LongMethod")
@Composable
fun HandyPlayApp(
    modifier: Modifier = Modifier,
    bottomSearchBarViewModel: BottomSearchBarViewModel = hiltViewModel(),
) {
    HandyPlayTheme {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        val bottomBarState by bottomSearchBarViewModel.uiState.collectAsStateWithLifecycle()

        Rebugger(
            composableName = "HandyPlayApp",
            trackMap = mapOf(
                "currentDestination" to currentDestination,
                "bottomBarState.isVisible" to bottomBarState.isVisible,
                "bottomBarState.searchQuery" to bottomBarState.searchQuery,
                "bottomBarState.pathSegments" to bottomBarState.pathSegments,
            ),
        )

        LaunchedEffect(currentDestination) {
            val screen = when {
                currentDestination?.hasRoute<HomeDestination>() == true ->
                    CurrentScreen.Home

                currentDestination?.hasRoute<CategoryDestination>() == true -> {
                    val categoryName = navBackStackEntry
                        ?.toRoute<CategoryDestination>()
                        ?.categoryName
                        .orEmpty()
                    CurrentScreen.Category(categoryName)
                }

                else -> CurrentScreen.Other
            }
            bottomSearchBarViewModel.onDestinationChanged(screen)
        }

        LaunchedEffect(Unit) {
            bottomSearchBarViewModel.navigationEvents.collect { event ->
                when (event) {
                    BottomSearchBarEvent.NavigateToHome -> navController.popBackStack(
                        route = HomeDestination,
                        inclusive = false,
                    )
                }
            }
        }

        Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding)) {
                HandyPlayNavHost(
                    navController = navController,
                    searchQuery = bottomBarState.searchQuery,
                    modifier = Modifier.weight(1f),
                )

                AnimatedVisibility(
                    visible = bottomBarState.isVisible,
                    enter = slideInVertically(tween(300)) { it } + fadeIn(tween(300)),
                    exit = slideOutVertically(tween(300)) { it } + fadeOut(tween(300)),
                ) {
                    BottomSearchBar(
                        query = bottomBarState.searchQuery,
                        onQueryChange = bottomSearchBarViewModel::onSearchQueryChanged,
                        pathSegments = bottomBarState.pathSegments,
                        onSegmentClick = bottomSearchBarViewModel::onSegmentClick,
                    )
                }
            }
        }
    }
}

@Composable
private fun HandyPlayNavHost(
    navController: NavHostController,
    searchQuery: String,
    modifier: Modifier = Modifier,
) {
    Rebugger(
        composableName = "HandyPlayNavHost",
        trackMap = mapOf(
            "searchQuery" to searchQuery,
        ),
    )

    NavHost(
        navController = navController,
        startDestination = WelcomeDestination,
        modifier = modifier,
        enterTransition = { fadeIn(tween(300)) + slideIntoContainer(Start, tween(300, easing = EaseOut)) },
        exitTransition = { fadeOut(tween(300)) + slideOutOfContainer(Start, tween(300, easing = EaseIn)) },
        popEnterTransition = { fadeIn(tween(300)) + slideIntoContainer(End, tween(300, easing = EaseOut)) },
        popExitTransition = { fadeOut(tween(300)) + slideOutOfContainer(End, tween(300, easing = EaseIn)) },
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
                searchQuery = searchQuery,
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
                searchQuery = searchQuery,
                onTopicClick = { topicId ->
                    when (topicId) {
                        Topic.ID_TTL_CACHE -> navController.navigate(TtlCacheDestination)
                        Topic.ID_GALLERY -> navController.navigate(GalleryDestination)
                    }
                },
            )
        }

        composable<TtlCacheDestination> {
            TtlCacheRoute()
        }

        composable<GalleryDestination> {
            GalleryRoute()
        }
    }
}
