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
import com.sls.handbook.feature.fever.FeverRoute
import com.sls.handbook.feature.gallery.GalleryRoute
import com.sls.handbook.feature.home.HomeRoute
import com.sls.handbook.feature.ttlcache.TtlCacheRoute
import com.sls.handbook.feature.dp.behavioral.command.CommandRoute
import com.sls.handbook.feature.dp.behavioral.observer.ObserverRoute
import com.sls.handbook.feature.dp.behavioral.statemachine.StateMachineRoute
import com.sls.handbook.feature.dp.behavioral.strategy.StrategyRoute
import com.sls.handbook.feature.dp.creational.abstractfactory.AbstractFactoryRoute
import com.sls.handbook.feature.dp.creational.factorymethod.FactoryMethodRoute
import com.sls.handbook.feature.dp.creational.prototype.PrototypeRoute
import com.sls.handbook.feature.dp.structural.adapter.AdapterPatternRoute
import com.sls.handbook.feature.dp.structural.decorator.DecoratorRoute
import com.sls.handbook.feature.dp.structural.facade.FacadeRoute
import com.sls.handbook.feature.welcome.WelcomeRoute
import com.sls.handbook.navigation.AbstractFactoryDestination
import com.sls.handbook.navigation.AdapterPatternDestination
import com.sls.handbook.navigation.CategoryDestination
import com.sls.handbook.navigation.CommandDestination
import com.sls.handbook.navigation.DecoratorDestination
import com.sls.handbook.navigation.FacadeDestination
import com.sls.handbook.navigation.FactoryMethodDestination
import com.sls.handbook.navigation.FeverDestination
import com.sls.handbook.navigation.GalleryDestination
import com.sls.handbook.navigation.HomeDestination
import com.sls.handbook.navigation.ObserverDestination
import com.sls.handbook.navigation.PrototypeDestination
import com.sls.handbook.navigation.StateMachineDestination
import com.sls.handbook.navigation.StrategyDestination
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

        val isEdgeToEdgeScreen = currentDestination?.hasRoute<FeverDestination>() == true

        Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
            Column(modifier = if (isEdgeToEdgeScreen) Modifier else Modifier.padding(innerPadding)) {
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

@Suppress("LongMethod")
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
                onTopicClick = { topic ->
                    when (topic) {
                        is Topic.KotlinFundamental.TtlCache -> navController.navigate(TtlCacheDestination)
                        is Topic.Ui.Gallery -> navController.navigate(GalleryDestination)
                        is Topic.Ui.Fever -> navController.navigate(FeverDestination)
                        is Topic.DesignPattern.FactoryMethod -> navController.navigate(FactoryMethodDestination)
                        is Topic.DesignPattern.AbstractFactory -> navController.navigate(AbstractFactoryDestination)
                        is Topic.DesignPattern.Prototype -> navController.navigate(PrototypeDestination)
                        is Topic.DesignPattern.Adapter -> navController.navigate(AdapterPatternDestination)
                        is Topic.DesignPattern.Decorator -> navController.navigate(DecoratorDestination)
                        is Topic.DesignPattern.Facade -> navController.navigate(FacadeDestination)
                        is Topic.DesignPattern.Observer -> navController.navigate(ObserverDestination)
                        is Topic.DesignPattern.Strategy -> navController.navigate(StrategyDestination)
                        is Topic.DesignPattern.Command -> navController.navigate(CommandDestination)
                        is Topic.DesignPattern.StateMachine -> navController.navigate(StateMachineDestination)
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

        composable<FeverDestination> {
            FeverRoute()
        }

        // Design Patterns - Creational
        composable<FactoryMethodDestination> {
            FactoryMethodRoute()
        }

        composable<AbstractFactoryDestination> {
            AbstractFactoryRoute()
        }

        composable<PrototypeDestination> {
            PrototypeRoute()
        }

        // Design Patterns - Structural
        composable<AdapterPatternDestination> {
            AdapterPatternRoute()
        }

        composable<DecoratorDestination> {
            DecoratorRoute()
        }

        composable<FacadeDestination> {
            FacadeRoute()
        }

        // Design Patterns - Behavioral
        composable<ObserverDestination> {
            ObserverRoute()
        }

        composable<StrategyDestination> {
            StrategyRoute()
        }

        composable<CommandDestination> {
            CommandRoute()
        }

        composable<StateMachineDestination> {
            StateMachineRoute()
        }
    }
}
