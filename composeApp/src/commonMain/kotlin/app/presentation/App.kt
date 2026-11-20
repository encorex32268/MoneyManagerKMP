@file:OptIn(KoinExperimentalAPI::class, ExperimentalMaterial3WindowSizeClassApi::class,
    ExperimentalMaterial3WindowSizeClassApi::class
)

package app.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import core.presentation.navigation.AppNavigationBottom
import core.presentation.navigation.AppNavigationRail
import core.presentation.navigation.NavigationLayoutType
import core.presentation.navigation.bottomNavigationItems
import core.presentation.navigation.calculateNavigationLayout
import feature.analytics.presentation.navigation.AnalyticsRoutes
import feature.analytics.presentation.navigation.analyticsGraph
import feature.chart.presentation.navigation.ChartRoutes
import feature.chart.presentation.navigation.chartGraph
import feature.home.presentation.navigation.HomeRoutes
import feature.home.presentation.navigation.homeGraph
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI


// LocalDarkLightMode moved to :core:designsystem AppTheme.kt

@Composable
fun App(
    onModeChanged: (isDarkMode: Boolean) -> Unit = {}
){

    val viewModel = koinViewModel<AppViewModel>()
    val appState by viewModel.state.collectAsStateWithLifecycle()
    CompositionLocalProvider( LocalDarkLightMode provides appState.isDarkMode){
        val darkLightMode = LocalDarkLightMode.current
        AppTheme(
            darkTheme = darkLightMode
        ){
            LaunchedEffect(appState){
                onModeChanged(appState.isDarkMode)
            }
            if (!appState.isMigrationReady) {
                return@AppTheme
            }
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination?.route
            var itemSelectedIndex by remember(currentDestination) {
                mutableStateOf(
                    when(currentDestination){
                        ChartRoutes.CHART -> 1
                        AnalyticsRoutes.ANALYTICS -> 2
                        else -> 0
                    }
                )
            }

            val windowSizeClass = calculateWindowSizeClass()
            val navigationLayoutType = windowSizeClass.calculateNavigationLayout(
                currentRoute = currentDestination,
            )
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background)
                    .systemBarsPadding()
                    .navigationBarsPadding()
                ,
                bottomBar = {
                    if (
                        isMainCurrentDestination(currentDestination)&&
                        navigationLayoutType == NavigationLayoutType.BOTTOM_NAVIGATION
                        )       {
                        AppNavigationBottom(
                            itemSelectedIndex = itemSelectedIndex,
                            onBarItemClick = { index , name ->
                                if(itemSelectedIndex != index){
                                    itemSelectedIndex = index
                                    navController.navigate(
                                        route = name,
                                        navOptions = when(name){
                                            HomeRoutes.HOME -> {
                                                navOptions {
                                                    popUpTo(navController.graph.startDestinationRoute?: HomeRoutes.HOME) {
                                                        inclusive = true
                                                    }
                                                    launchSingleTop = true
                                                }
                                            }
                                            else -> {
                                                navOptions {
                                                    popUpTo(HomeRoutes.HOME) {
                                                        inclusive = false
                                                    }
                                                    launchSingleTop = true
                                                }
                                            }
                                        }

                                    )
                                }

                            }
                        )
                    }
                }
            ){ paddingValues ->
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    if (
                        isMainCurrentDestination(currentDestination)&&
                        navigationLayoutType == NavigationLayoutType.NAVIGATION_RAIL)     {
                        Row {
                            AppNavigationRail(
                                itemSelectedIndex = itemSelectedIndex,
                                onBarItemClick = { index, name ->
                                    if(itemSelectedIndex != index){
                                        itemSelectedIndex = index
                                        navController.navigate(
                                            route = name,
                                            navOptions = when(name){
                                                HomeRoutes.HOME -> {
                                                    navOptions {
                                                        popUpTo(navController.graph.startDestinationRoute?: HomeRoutes.HOME) {
                                                            inclusive = true
                                                        }
                                                        launchSingleTop = true
                                                    }
                                                }
                                                else -> {
                                                    navOptions {
                                                        popUpTo(HomeRoutes.HOME) {
                                                            inclusive = false
                                                        }
                                                        launchSingleTop = true
                                                    }
                                                }
                                            }
                                        )
                                    }
                                }
                            )
                            VerticalDivider(
                                modifier = Modifier.fillMaxHeight()
                                    .wrapContentWidth(),
                                color = MaterialTheme.colorScheme.surfaceContainerHighest,
                            )
                        }

                    }
                    NavHost(
                        modifier = Modifier.fillMaxSize(),
                        navController = navController,
                        startDestination = HomeRoutes.GRAPH
                    ){

                        homeGraph(
                            navController = navController,
                            navigationLayoutType = navigationLayoutType
                        )
                        chartGraph(
                            navController = navController,
                            navigationLayoutType = navigationLayoutType
                        )
                        analyticsGraph(
                            navController = navController,
                            onDarkLightModeSwitch = {
                                viewModel.onAction(AppEvent.DarkLightChange)
                            }
                        )
                    }
                }

            }
        }

    }

}


private fun isMainCurrentDestination(currentDestination: String?): Boolean{
    return currentDestination in bottomNavigationItems.map { it.name }
}


