package feature.home.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import androidx.navigation.navigation
import androidx.navigation.toRoute
import app.presentation.navigation.Route
import core.domain.mapper.toType
import core.domain.mapper.toTypeUi
import core.domain.model.Expense
import core.domain.model.Type
import core.presentation.navigation.NavigationLayoutType
import feature.home.domain.navigation.ExpenseNavType
import feature.home.domain.navigation.TypeNavType
import feature.home.presentation.HomeScreenRoot
import feature.home.presentation.add.AddScreenRoot
import feature.home.presentation.add.type.TypesScreenRoot
import feature.home.presentation.add.type.category.TypeCategoryEditScreenRoot
import feature.home.presentation.edit.EditExpenseScreenRoot
import kotlin.reflect.typeOf


fun NavGraphBuilder.homeGraph(
    navController: NavHostController,
    navigationLayoutType: NavigationLayoutType
) {
    navigation(
        route = Route.HomeGraph.route,
        startDestination = Route.Home.route
    ) {
        composable(Route.Home.route) {
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