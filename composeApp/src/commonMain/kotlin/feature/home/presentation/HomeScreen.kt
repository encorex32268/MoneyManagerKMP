@file:OptIn(KoinExperimentalAPI::class, ExperimentalMaterial3Api::class)

package feature.home.presentation

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
import feature.core.domain.model.Expense
import feature.core.presentation.components.DatePicker
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
){
    val state by viewModel.state.collectAsState()

    HomeScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onGotoAddScreen = onGotoAddScreen,
        onGotoChartScreen = onGotoChartScreen,
        onGotoEditScreen = onGotoEditScreen
    )
}


@Composable
fun HomeScreen(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit,
    onGotoAddScreen: () -> Unit = {},
    onGotoChartScreen: () -> Unit = {},
    onGotoEditScreen: (Expense) -> Unit  = {}
){
    Scaffold(
        containerColor = Color.White,
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = onGotoAddScreen,
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
                    onEvent(
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
                    .noRippleClick(
                        onClick = onGotoChartScreen
                    )
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
                    key = { it }
                ) {(_ , expenses) ->
                    ExpenseItem(
                        modifier = Modifier.fillMaxWidth(),
                        items = expenses,
                        onItemClick = onGotoEditScreen,
                        types = state.typesItem
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
    }

}