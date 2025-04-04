package feature.chart.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import androidx.navigation.navigation
import androidx.navigation.toRoute
import app.presentation.navigation.Route
import feature.chart.presentation.ChartScreenRoot
import feature.chart.presentation.chartdetail.DetailScreenRoot
import core.domain.model.Expense
import core.domain.model.Type
import core.presentation.navigation.NavigationLayoutType
import feature.home.domain.navigation.ExpenseListNavType
import feature.home.domain.navigation.TypeNavType
import kotlin.reflect.typeOf

fun NavGraphBuilder.chartGraph(
    navController: NavHostController,
    navigationLayoutType: NavigationLayoutType
) {
    navigation(
        route = Route.ChartGraph.route,
        startDestination = Route.Chart.route
    ) {
        composable(Route.Chart.route) {
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