package feature.home.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import androidx.navigation.navigation
import androidx.navigation.toRoute
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
        route = HomeRoutes.GRAPH,
        startDestination = HomeRoutes.HOME
    ) {
        composable(HomeRoutes.HOME) {
            HomeScreenRoot(
                onGotoAddScreen = {
                    navController.navigate(
                        HomeRoutes.HomeAdd(
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
                        HomeRoutes.HomeEdit(
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
        composable<HomeRoutes.HomeAdd>(
            typeMap = mapOf(
                typeOf<Expense>() to ExpenseNavType,
                typeOf<Boolean>() to NavType.BoolType
            )
        ) {
            val homeAdd = it.toRoute<HomeRoutes.HomeAdd>()
            AddScreenRoot(
                expense = if (homeAdd.isAddNew) null else homeAdd.expense,
                onGoBack = {
                    navController.navigateUp()
                },
                onGoToCategoryEditClick = {
                    navController.navigate(
                        route = HomeRoutes.Types,
                        navOptions = navOptions {
                            launchSingleTop = true
                        }
                    )
                },
                navigationLayoutType = navigationLayoutType
            )
        }
        composable<HomeRoutes.HomeEdit>(
            typeMap = mapOf(
                typeOf<Expense>() to ExpenseNavType
            )
        ) {
            val expense = it.toRoute<HomeRoutes.HomeEdit>().expense
            EditExpenseScreenRoot(
                expense = expense,
                onGoBack = {
                    navController.navigateUp()
                },
                onGotoAddScreen = { toAddExpense ->
                    navController.navigate(
                        HomeRoutes.HomeAdd(
                            expense = toAddExpense,
                            isAddNew = false
                        )
                    )
                }
            )
        }
        composable<HomeRoutes.Types>(
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
                        HomeRoutes.TypeCategoryEdit(it.toType()),
                        navOptions = navOptions {
                            launchSingleTop = true
                        }
                    )
                }
            )
        }

        composable<HomeRoutes.TypeCategoryEdit>(
            typeMap = mapOf(
                typeOf<Type>() to TypeNavType
            )
        ) {
            val typeUi = it.toRoute<HomeRoutes.TypeCategoryEdit>().type.toTypeUi()
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