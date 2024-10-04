@file:OptIn(KoinExperimentalAPI::class)

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import androidx.navigation.toRoute
import feature.chart.presentation.ChartScreenRoot
import feature.chart.presentation.chartdetail.DetailScreenRoot
import feature.core.domain.mapper.toType
import feature.core.domain.mapper.toTypeUi
import feature.core.domain.model.Expense
import feature.core.domain.model.Type
import feature.core.presentation.navigation.bottomNavigationItems
import feature.home.presentation.HomeScreenRoot
import feature.home.presentation.add.AddScreenRoot
import feature.home.presentation.add.type.TypesScreenRoot
import feature.home.presentation.add.type.category.TypeCategoryEditScreenRoot
import feature.home.presentation.edit.EditExpenseScreenRoot
import feature.home.domain.navigation.ExpenseListNavType
import feature.home.domain.navigation.ExpenseNavType
import feature.home.domain.navigation.TypeNavType
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import kotlin.reflect.typeOf

@OptIn(ExperimentalResourceApi::class)
@Composable
@Preview
fun App(
    appViewModel:AppViewModel = koinViewModel<AppViewModel>()
) {
    AppTheme {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination?.route
        var itemSelectedIndex by remember {
            mutableStateOf(0)
        }
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .navigationBarsPadding(),
            bottomBar = {
                AnimatedVisibility(
                    visible = isMainCurrentDestination(currentDestination),
                    enter = slideInVertically(),
                    exit = slideOutVertically(),
                ){
                    NavigationBar(
                        modifier = Modifier.height(56.dp),
                        containerColor = Color.White
                    ) {
                        bottomNavigationItems.forEachIndexed { index, bottomNavigationItem ->
                            NavigationBarItem(
                                colors = NavigationBarItemDefaults.colors(
                                    indicatorColor = Color.Transparent
                                ),
                                selected = (itemSelectedIndex == index),
                                onClick = {
                                    itemSelectedIndex = index
                                    navController.navigate(
                                        bottomNavigationItem.name,
                                        navOptions = navOptions {
                                            launchSingleTop = true
                                        }
                                    )
                                },
                                icon = {
                                    Icon(
                                        painter = painterResource(
                                            if (itemSelectedIndex == index){
                                                bottomNavigationItem.selectedIcon
                                            }else{
                                                bottomNavigationItem.unSelectedIcon
                                            }
                                        ),
                                        contentDescription = bottomNavigationItem.name,
                                    )
                                }
                            )
                        }
                    }
                }
            }
        ){
            NavHost(
                modifier = Modifier.padding(
                    bottom = if (isMainCurrentDestination(currentDestination))
                        56.dp
                    else 0.dp
                ),
                navController = navController,
                startDestination = Route.Home
            ){
                composable<Route.Home>{
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
                            itemSelectedIndex = 1
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
                        }
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
                        expense = if(homeAdd.isAddNew) null else homeAdd.expense,
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
                        }
                    )
                }
                composable<Route.HomeEdit>(
                    typeMap = mapOf(
                        typeOf<Expense>() to ExpenseNavType
                    )
                ){
                    val expense = it.toRoute<Route.HomeEdit>().expense
                    EditExpenseScreenRoot(
                        expense = expense,
                        onGoBack = {
                            navController.navigateUp()
                        },
                        onGotoAddScreen = {
                            navController.navigate(
                                Route.HomeAdd(
                                    expense = expense,
                                    isAddNew = false
                                )
                            )
                        }
                    )
                }
                composable<Route.Chart>{
                    ChartScreenRoot(
                        onGotoDetail = {
                            navController.navigate(
                                Route.ChartDetail(
                                    items = it.expenseItems
                                ),
                                navOptions = navOptions {
                                    launchSingleTop = true
                                }
                            )
                        }
                    )
                }
                composable<Route.ChartDetail>(
                    typeMap = mapOf(
                        typeOf<List<Expense>>() to ExpenseListNavType
                    )
                ){
                    val chartDetail = it.toRoute<Route.ChartDetail>()
                    DetailScreenRoot(
                        items = chartDetail.items,
                        onBack = {
                            navController.navigateUp()
                        }
                    )
                }

                composable<Route.Types>(
                    typeMap = mapOf(
                        typeOf<Type>() to TypeNavType
                    )
                ){
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
                ){
                    val typeUi = it.toRoute<Route.TypeCategoryEdit>().type.toTypeUi()
                    TypeCategoryEditScreenRoot(
                        typeUi = typeUi,
                        onBack = {
                            navController.navigateUp()
                        }
                    )
                }
            }
        }
    }
}

fun isMainCurrentDestination(currentDestination: String?): Boolean{
    return (currentDestination in bottomNavigationItems.map { it.name })
}




