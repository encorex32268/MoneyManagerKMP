@file:OptIn(KoinExperimentalAPI::class, ExperimentalMaterial3Api::class)

package feature.home.presentation

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import core.domain.model.Expense
import core.presentation.components.DatePicker
import core.presentation.navigation.NavigationLayoutType
import feature.home.presentation.components.AmountTextLayout
import feature.home.presentation.components.ExpenseItem
import core.presentation.noRippleClick
import core.ui.textColor
import feature.home.domain.mapper.toExpense
import feature.home.presentation.components.SpendingLimitDialog
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI


@Composable
fun HomeScreenRoot(
    viewModel: HomeViewModel = koinViewModel(),
    onGotoAddScreen: () -> Unit = {},
    onGotoEditScreen: (Expense) -> Unit  = {},
    navigationLayoutType: NavigationLayoutType = NavigationLayoutType.BOTTOM_NAVIGATION
){
    val state by viewModel.state.collectAsStateWithLifecycle()

    var isShowExpenseLimitDialog by remember{
        mutableStateOf(false)
    }
    HomeScreen(
        state = state,
        onAction = { event ->
            when(event){
                HomeAction.OnGotoAddScreen   -> onGotoAddScreen()
                is HomeAction.OnGotoEditScreen  -> {
                    onGotoEditScreen(event.expenseUi.toExpense())
                }
                HomeAction.OnExpenseLimitClick -> {
                    isShowExpenseLimitDialog = true
                }
                else -> Unit
            }
            viewModel.onAction(event)
        },
        navigationLayoutType = navigationLayoutType,
    )
    if (isShowExpenseLimitDialog){
        SpendingLimitDialog(
            onDismissRequest = {
                isShowExpenseLimitDialog = false
            },
            onConfirmButtonClick = {
                viewModel.onAction(
                    HomeAction.OnSpendingLimitChange(it)
                )
            }
        )
    }
}


@Composable
fun HomeScreen(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
    isDebug: Boolean = false,
    navigationLayoutType: NavigationLayoutType = NavigationLayoutType.BOTTOM_NAVIGATION,
){
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onAction(HomeAction.OnGotoAddScreen)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription =null
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
                modifier = Modifier.fillMaxWidth().height(48.dp),
                year = state.nowDateYear.toIntOrNull()?:0,
                month = state.nowDateMonth.toIntOrNull()?:0,
                onDateChange = { year , month ->
                    onAction(
                        HomeAction.OnDatePick(
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
                        onAction = onAction,
                        onExpenseLimitClick = {
                            onAction(HomeAction.OnExpenseLimitClick)
                        }     ,
                        isDebug = isDebug
                    )
                }
                else -> {
                    HomeScreenNaviRail(
                        state = state,
                        onAction = onAction,
                        onExpenseLimitClick = {
                            onAction(HomeAction.OnExpenseLimitClick)
                        }                   ,
                        isDebug = isDebug
                    )
                }
            }



        }
    }

}

@Composable
private fun HomeScreenNaviBottom(
    state: HomeState,
    isDebug: Boolean,
    onAction: (HomeAction) -> Unit = {},
    onExpenseLimitClick: () -> Unit = {},
    modifier: Modifier = Modifier
){
    val listState = rememberLazyListState()
    LaunchedEffect(state.items.size){
        if (state.items.isNotEmpty()){
            listState.scrollToItem(0)
        }
    }
    Column(
        modifier =  modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        AmountTextLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            totalExpense = state.totalExpense,
            expenseLimit = state.expenseLimit,
            navigationLayoutType = NavigationLayoutType.BOTTOM_NAVIGATION,
            onExpenseLimitClick = onExpenseLimitClick,
            limitOverColor = state.limitOverColor
        )
        Spacer(modifier = Modifier.height(14.dp))
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            state = listState
        ){
            items(
                items = state.items,
                key = { it }
            ) {(_ , expenses) ->
                ExpenseItem(
                    modifier = Modifier.fillMaxWidth(),
                    items = expenses,
                    onItemClick = { expenseUi ->
                        onAction(HomeAction.OnGotoEditScreen(expenseUi = expenseUi))
                    }
                )
            }
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun HomeScreenNaviRail(
    state: HomeState,
    isDebug: Boolean,
    onAction: (HomeAction) -> Unit = {},
    onExpenseLimitClick: () -> Unit = {}

){
    val listState = rememberLazyListState()
    LaunchedEffect(state.items.size){
        if (state.items.isNotEmpty()){
            listState.scrollToItem(0)
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth()
    ){
        AmountTextLayout(
            modifier = Modifier
                .width(IntrinsicSize.Max),
            totalExpense = state.totalExpense,
            expenseLimit = state.expenseLimit,
            navigationLayoutType = NavigationLayoutType.NAVIGATION_RAIL,
            onExpenseLimitClick = onExpenseLimitClick,
            limitOverColor = state.limitOverColor
        )
        Spacer(modifier = Modifier.width(8.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = listState
        ){
            items(
                items = state.items,
                key = { it }
            ) {(_ , expenses) ->
                ExpenseItem(
                    modifier = Modifier.fillMaxWidth(),
                    items = expenses,
                    onItemClick = { expenseUi ->
                        onAction(HomeAction.OnGotoEditScreen(
                            expenseUi = expenseUi
                        ))
                    }
                )
            }
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@org.jetbrains.compose.ui.tooling.preview.Preview
@Composable
private fun HomeScreenPreview() {
    app.presentation.AppTheme {
        HomeScreen(
            state = HomeState(
                totalExpense = 1500,
                expenseLimit = 20000,
                nowDateYear = "2026",
                nowDateMonth = "11",
                nowDateDayOfMonth = "21"
            ),
            onAction = {},
            isDebug = true
        )
    }
}