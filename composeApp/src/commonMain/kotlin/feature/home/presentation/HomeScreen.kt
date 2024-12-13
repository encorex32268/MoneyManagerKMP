@file:OptIn(KoinExperimentalAPI::class, ExperimentalMaterial3Api::class)

package feature.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lihan.moneymanager.BuildKonfig
import feature.core.domain.model.Expense
import feature.core.presentation.components.DatePicker
import feature.core.presentation.navigation.NavigationLayoutType
import feature.home.presentation.components.AmountTextLayout
import feature.home.presentation.components.ExpenseItem
import feature.core.presentation.noRippleClick
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI


@Composable
fun HomeScreenRoot(
    viewModel: HomeViewModel = koinViewModel(),
    onGotoAddScreen: () -> Unit = {},
    onGotoChartScreen: () -> Unit = {},
    onGotoEditScreen: (Expense) -> Unit  = {},
    navigationLayoutType: NavigationLayoutType = NavigationLayoutType.BOTTOM_NAVIGATION
){
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomeScreen(
        state = state,
        onEvent = { event ->
            when(event){
                HomeEvent.OnGotoAddScreen   -> onGotoAddScreen()
                HomeEvent.OnGotoChartScreen -> onGotoChartScreen()
                is HomeEvent.OnGotoEditScreen  -> {
                    onGotoEditScreen(event.expense)
                }
                else -> Unit
            }
            viewModel.onEvent(event)
        },
        navigationLayoutType = navigationLayoutType
    )
}


@Composable
fun HomeScreen(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit,
    isDebug: Boolean = false,
    navigationLayoutType: NavigationLayoutType = NavigationLayoutType.BOTTOM_NAVIGATION
){
    Scaffold(
        containerColor = Color.White,
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onEvent(HomeEvent.OnGotoAddScreen)
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
                .background(Color.White)
                .padding(it)
        ) {
            DatePicker(
                year = state.nowDateYear.toIntOrNull()?:0,
                month = state.nowDateMonth.toIntOrNull()?:0,
                onDateChange = { year , month ->
                    onEvent(
                        HomeEvent.OnDatePick(
                            year = year,
                            month = month
                        )
                    )
                }
            )
            Spacer(modifier = Modifier.height(4.dp))
            when(navigationLayoutType){
                NavigationLayoutType.BOTTOM_NAVIGATION -> {
                    HomeScreenNaviBottom(
                        state = state,
                        onEvent = onEvent
                    )
                }
                else -> {
                    HomeScreenNaviRail(
                        state = state,
                        onEvent = onEvent
                    )
                }
            }

            if (!isDebug){
                AdMobBannerController.AdMobBannerCompose(
                    modifier = Modifier.fillMaxWidth()
                )
            }

        }
    }

}

@Composable
private fun HomeScreenNaviBottom(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit = {}
){
    Column {
        AmountTextLayout(
            modifier = Modifier
                .fillMaxWidth()
                .noRippleClick(
                    onClick = {
                        onEvent(HomeEvent.OnGotoChartScreen)
                    }
                )
            ,
            income = state.income,
            expense = state.expense,
            total = state.totalAmount,
            navigationLayoutType = NavigationLayoutType.BOTTOM_NAVIGATION
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            items(
                items = state.items,
                key = { it }
            ) {(_ , expenses) ->
                ExpenseItem(
                    modifier = Modifier.fillMaxWidth(),
                    items = expenses,
                    onItemClick = { expense ->
                        onEvent(HomeEvent.OnGotoEditScreen(expense = expense))
                    },
                    types = state.typesItem
                )
            }
            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }

}


@Composable
private fun HomeScreenNaviRail(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit = {}
){
    Row(
        modifier = Modifier.fillMaxWidth()
    ){
        AmountTextLayout(
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .noRippleClick(
                    onClick = {
                        onEvent(HomeEvent.OnGotoChartScreen)
                    }
                )
            ,
            income = state.income,
            expense = state.expense,
            total = state.totalAmount,
            navigationLayoutType = NavigationLayoutType.NAVIGATION_RAIL
        )
        Spacer(modifier = Modifier.width(8.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            items(
                items = state.items,
                key = { it }
            ) {(_ , expenses) ->
                ExpenseItem(
                    modifier = Modifier.fillMaxWidth(),
                    items = expenses,
                    onItemClick = { expense ->
                        onEvent(HomeEvent.OnGotoEditScreen(expense = expense))
                    },
                    types = state.typesItem
                )
            }
            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}