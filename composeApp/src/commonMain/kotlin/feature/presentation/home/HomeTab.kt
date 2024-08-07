@file:OptIn(ExperimentalResourceApi::class)

package feature.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import feature.presentation.add.AddScreen
import feature.core.navigation.CustomTab
import feature.core.navigation.CustomTabOptions
import feature.core.presentation.components.DatePicker
import feature.presentation.chart.ChartTab
import feature.presentation.edit.EditExpenseScreen
import feature.presentation.home.components.AmountTextLayout
import feature.presentation.home.components.ExpenseItem
import feature.presentation.noRippleClick
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.baseline_receipt_24_filled
import moneymanagerkmp.composeapp.generated.resources.baseline_receipt_24_outline
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@ExperimentalResourceApi
object HomeTab : CustomTab {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val navigatorTab = LocalTabNavigator.current
        val homeScreenModel = getScreenModel<HomeScreenModel>()
        val state by homeScreenModel.state.collectAsState()
        LaunchedEffect(Unit){
            homeScreenModel.onEvent(
                HomeEvent.OnDatePick(
                    isInit = true
                )
            )
        }
        Scaffold(
            containerColor = Color.White,
            modifier = Modifier.fillMaxSize(),
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        navigator.parent?.push(AddScreen(null))
                    },
                    containerColor = MaterialTheme.colorScheme.onBackground
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription =null,
                        tint = MaterialTheme.colorScheme.background
                    )
                }
            }
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                DatePicker(
                    year = state.nowDateYear.toIntOrNull()?:0,
                    month = state.nowDateMonth.toIntOrNull()?:0,
                    onDateChange = { year , month ->
                        homeScreenModel.onEvent(
                            HomeEvent.OnDatePick(
                                year = year,
                                month = month
                            )
                        )
                    }
                )
                Spacer(modifier = Modifier.height(4.dp))
                AmountTextLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .noRippleClick {
                            navigatorTab.current = ChartTab
                        }
                    ,
                    income = state.income,
                    expense = state.expense,
                    total = state.totalAmount
                )
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ){
                items(
                    items = state.items,
                    key = {
                        it
                    }
                ) {(_ , expenses) ->
                    ExpenseItem(
                        modifier = Modifier
                            .fillMaxWidth(),
                        items = expenses,
                        onItemClick = {
                            navigator.parent?.push(
                                EditExpenseScreen(it)
                            )
                        }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
            }
        }
    }

    override val customTabOptions: CustomTabOptions
        @Composable
        get() {
            val selectedIcon = painterResource(Res.drawable.baseline_receipt_24_filled)
            val unSelectedIcon = painterResource(Res.drawable.baseline_receipt_24_outline)
            return remember{
                CustomTabOptions(
                    index = 0u,
                    title = "Home",
                    selectedIcon = selectedIcon,
                    unSelectedIcon = unSelectedIcon
                )
            }
        }


}