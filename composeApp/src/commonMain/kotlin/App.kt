
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import androidx.navigation.toRoute
import feature.chart.ChartScreenRoot
import feature.chart.chartdetail.DetailScreenRoot
import feature.core.domain.model.Expense
import feature.core.presentation.navigation.bottomNavigationItems
import feature.home.HomeScreen
import feature.home.HomeScreenRoot
import feature.home.add.AddScreenRoot
import feature.home.edit.EditExpenseScreenRoot
import feature.home.navigation.ExpenseListNavType
import feature.home.navigation.ExpenseNavType
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.reflect.typeOf

@OptIn(ExperimentalResourceApi::class)
@Composable
@Preview
fun App() {
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
                if (currentDestination in bottomNavigationItems.map { it.name }){
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
                                    navController.navigate(bottomNavigationItem.name)
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
                modifier = Modifier.padding(bottom = 56.dp),
                navController = navController,
                startDestination = Home
            ){
                composable<Home>{
                    HomeScreenRoot(
                        onGotoAddScreen = {
                            navController.navigate(
                                HomeAdd(
                                    expense = Expense(),
                                    isAddNew = true
                                )
                            )
                        },
                        onGotoChartScreen = {
                            navController.navigate("Chart")
                        },
                        onGotoEditScreen = {
                            navController.navigate(
                                HomeAdd(
                                    expense = it,
                                    isAddNew = false
                                )
                            )
                        }
                    )
                }
                composable<HomeAdd>(
                    typeMap = mapOf(
                        typeOf<Expense>() to ExpenseNavType,
                        typeOf<Boolean>() to NavType.BoolType
                    )
                ) {
                    val homeAdd = it.toRoute<HomeAdd>()
                    AddScreenRoot(
                        expense = if(homeAdd.isAddNew) null else homeAdd.expense,
                        onGoBack = {
                            navController.popBackStack()
                        }
                    )
                }
                composable<HomeEdit>(
                    typeMap = mapOf(
                        typeOf<Expense>() to ExpenseNavType
                    )
                ){
                    val expense = it.toRoute<Expense>()
                    EditExpenseScreenRoot(
                        expense = expense,
                        onGoBack = {
                            navController.popBackStack()
                        },
                        onGotoAddScreen = {
                            navController.navigate(
                                HomeAdd(
                                    expense = expense,
                                    isAddNew = false
                                )
                            )
                        }
                    )
                }
                composable<Chart>{
                    ChartScreenRoot(
                        onGotoDetail = {
                            navController.navigate(
                                ChartDetail(
                                    items = it.expenseItems
                                )
                            )
                        }
                    )
                }
                composable<ChartDetail>(
                    typeMap = mapOf(
                        typeOf<List<Expense>>() to ExpenseListNavType
                    )
                ){
                    val chartDetail = it.toRoute<ChartDetail>()
                    DetailScreenRoot(
                        items = chartDetail.items,
                        onBack = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}


