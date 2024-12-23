@file:OptIn(KoinExperimentalAPI::class, ExperimentalMaterial3WindowSizeClassApi::class,
    ExperimentalMaterial3WindowSizeClassApi::class
)

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.util.fastAny
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import androidx.navigation.navigation
import androidx.navigation.toRoute
import feature.analytics.presentation.AnalyticsScreenRoot
import feature.analytics.presentation.backup.BackupScreenRoot
import feature.chart.presentation.ChartScreenRoot
import feature.chart.presentation.chartdetail.DetailScreenRoot
import feature.core.domain.mapper.toType
import feature.core.domain.mapper.toTypeUi
import feature.core.domain.model.Expense
import feature.core.domain.model.Type
import feature.core.presentation.navigation.AppNavigationBottom
import feature.core.presentation.navigation.AppNavigationRail
import feature.core.presentation.navigation.NavigationLayoutType
import feature.core.presentation.navigation.bottomNavigationItems
import feature.core.presentation.navigation.calculateNavigationLayout
import feature.home.domain.navigation.ExpenseListNavType
import feature.home.domain.navigation.ExpenseNavType
import feature.home.domain.navigation.TypeNavType
import feature.home.presentation.HomeScreenRoot
import feature.home.presentation.add.AddScreenRoot
import feature.home.presentation.add.type.TypesScreenRoot
import feature.home.presentation.add.type.category.TypeCategoryEditScreenRoot
import feature.home.presentation.edit.EditExpenseScreenRoot
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import kotlin.reflect.typeOf


val LocalDarkLightMode = compositionLocalOf { false }


@Composable
@Preview
fun App(){
    val viewModel = koinViewModel<AppViewModel>()
    val appState by viewModel.state.collectAsStateWithLifecycle()

    CompositionLocalProvider( LocalDarkLightMode provides appState.isDarkMode){
        AppTheme(
            darkTheme = LocalDarkLightMode.current
        ){
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination?.route
            var itemSelectedIndex by remember(currentDestination) {
                mutableStateOf(
                    when(currentDestination){
                        "Route.Chart"->1
                        "Route.Analytics" -> 2
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
                    AnimatedVisibility(
                        visible = isMainCurrentDestination(currentDestination) && (navigationLayoutType == NavigationLayoutType.BOTTOM_NAVIGATION),
                        enter = slideInVertically(),
                        exit = slideOutVertically(),
                    ){
                        AppNavigationBottom(
                            itemSelectedIndex = itemSelectedIndex,
                            onBarItemClick = { index , name ->
                                if(itemSelectedIndex != index){
                                    itemSelectedIndex = index
                                    navController.navigate(
                                        route = name,
                                        navOptions = when(name){
                                            "Route.Home" -> {
                                                navOptions {
                                                    popUpTo(navController.graph.startDestinationId) {
                                                        inclusive = true
                                                    }
                                                    launchSingleTop = true
                                                }
                                            }
                                            else -> {
                                                navOptions {
                                                    popUpTo("Route.Home") {
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
                    AnimatedVisibility(
                        visible = (isMainCurrentDestination(currentDestination) && navigationLayoutType == NavigationLayoutType.NAVIGATION_RAIL),
                        enter = slideInHorizontally(initialOffsetX = { -it }),
                        exit = shrinkHorizontally() + fadeOut(),
                    ) {
                        Row {
                            AppNavigationRail(
                                itemSelectedIndex = itemSelectedIndex,
                                onBarItemClick = { index, name ->
                                    if(itemSelectedIndex != index){
                                        itemSelectedIndex = index
                                        navController.navigate(
                                            route = name,
                                            navOptions = when(name){
                                                "Route.Home" -> {
                                                    navOptions {
                                                        popUpTo(navController.graph.startDestinationId) {
                                                            inclusive = true
                                                        }
                                                        launchSingleTop = true
                                                    }
                                                }
                                                else -> {
                                                    navOptions {
                                                        popUpTo("Route.Home") {
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
                        startDestination = Route.HomeGraph
                    ){

                        homeGraph(
                            navController = navController,
                            onChartClick = {
                                itemSelectedIndex = 1
                            },
                            navigationLayoutType = navigationLayoutType
                        )
                        chartGraph(
                            navController = navController,
                            navigationLayoutType = navigationLayoutType
                        )
                        analyticsGraph(
                            navController = navController,
                            onDarkLightModeSwitch = {
                                viewModel.onEvent(AppEvent.DarkLightChange)
                            }
                        )
                    }
                }

            }
        }

    }

}




private fun NavGraphBuilder.analyticsGraph(
    navController: NavHostController,
    onDarkLightModeSwitch: () -> Unit = {}
) {
    navigation<Route.AnalyticsGraph>(
        startDestination = Route.Analytics
    ){
        composable<Route.Analytics> {
            AnalyticsScreenRoot(
                onBackupClick = {
                    navController.navigate(
                        Route.Backup
                    )
                },
                onDarkLightModeSwitch = onDarkLightModeSwitch
            )
        }
        composable<Route.Backup> {
            BackupScreenRoot(
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }
    }

}

private fun NavGraphBuilder.chartGraph(
    navController: NavHostController,
    navigationLayoutType: NavigationLayoutType
) {
    navigation<Route.ChartGraph>(
        startDestination = Route.Chart
    ) {
        composable<Route.Chart> {
            ChartScreenRoot(
                onGotoDetail = { items, type ->
                    navController.navigate(
                        Route.ChartDetail(
                            items = items,
                            type = type
                        ),
                        navOptions = navOptions {
                            launchSingleTop = true
                        }
                    )
                },
                navigationLayoutType = navigationLayoutType
            )
        }
        composable<Route.ChartDetail>(
            typeMap = mapOf(
                typeOf<List<Expense>>() to ExpenseListNavType,
                typeOf<Type>() to TypeNavType
            )
        ) {
            val chartDetail = it.toRoute<Route.ChartDetail>()
            DetailScreenRoot(
                items = chartDetail.items,
                type = chartDetail.type,
                onBack = {
                    navController.navigateUp()
                }
            )
        }

    }
}

private fun NavGraphBuilder.homeGraph(
    navController: NavHostController,
    onChartClick: () -> Unit = {},
    navigationLayoutType: NavigationLayoutType
) {
    navigation<Route.HomeGraph>(
        startDestination = Route.Home
    ) {
        composable<Route.Home> {
            HomeScreenRoot(
                onGotoAddScreen = {
                    navController.navigate(
                        Route.HomeAdd(
                            expense = Expense(),
                            isAddNew = true
                        ),
                        navOptions = navOptions {
                            launchSingleTop = true
                        }
                    )
                },
                onGotoChartScreen = {
                    onChartClick()
                    navController.navigate(
                        Route.Chart,
                        navOptions = navOptions {
                            launchSingleTop = true
                        }
                    )
                },
                onGotoEditScreen = {
                    navController.navigate(
                        Route.HomeEdit(
                            expense = it
                        ),
                        navOptions = navOptions {
                            launchSingleTop = true
                        }
                    )
                },
                navigationLayoutType = navigationLayoutType
            )
        }
        composable<Route.HomeAdd>(
            typeMap = mapOf(
                typeOf<Expense>() to ExpenseNavType,
                typeOf<Boolean>() to NavType.BoolType
            )
        ) {
            val homeAdd = it.toRoute<Route.HomeAdd>()
            AddScreenRoot(
                expense = if (homeAdd.isAddNew) null else homeAdd.expense,
                onGoBack = {
                    navController.navigateUp()
                },
                onGoToCategoryEditClick = {
                    navController.navigate(
                        route = Route.Types,
                        navOptions = navOptions {
                            launchSingleTop = true
                        }
                    )
                },
                navigationLayoutType = navigationLayoutType
            )
        }
        composable<Route.HomeEdit>(
            typeMap = mapOf(
                typeOf<Expense>() to ExpenseNavType
            )
        ) {
            val expense = it.toRoute<Route.HomeEdit>().expense
            EditExpenseScreenRoot(
                expense = expense,
                onGoBack = {
                    navController.navigateUp()
                },
                onGotoAddScreen = { toAddExpense ->
                    navController.navigate(
                        Route.HomeAdd(
                            expense = toAddExpense,
                            isAddNew = false
                        )
                    )
                }
            )
        }
        composable<Route.Types>(
            typeMap = mapOf(
                typeOf<Type>() to TypeNavType
            )
        ) {
            TypesScreenRoot(
                onBack = {
                    navController.navigateUp()
                },
                navigateToTypeCategoryEdit = {
                    navController.navigate(
                        Route.TypeCategoryEdit(it.toType()),
                        navOptions = navOptions {
                            launchSingleTop = true
                        }
                    )
                }
            )
        }

        composable<Route.TypeCategoryEdit>(
            typeMap = mapOf(
                typeOf<Type>() to TypeNavType
            )
        ) {
            val typeUi = it.toRoute<Route.TypeCategoryEdit>().type.toTypeUi()
            TypeCategoryEditScreenRoot(
                typeUi = typeUi,
                onBack = {
                    navController.navigateUp()
                },
                navigationLayoutType = navigationLayoutType
            )
        }

    }
}

private fun isMainCurrentDestination(currentDestination: String?): Boolean{
    return currentDestination in bottomNavigationItems.map { it.name }
}



