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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import core.domain.model.Expense
import core.presentation.components.DatePicker
import core.presentation.components.MoneyEmptyState
import app.presentation.AppTheme
import core.presentation.navigation.NavigationLayoutType
import core.ui.MoneyTheme
import core.ui.MoneyTokens
import feature.home.domain.mapper.toExpense
import feature.home.presentation.components.AmountTextLayout
import feature.home.presentation.components.ExpenseItem
import feature.home.presentation.components.SpendingLimitDialog
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.baseline_receipt_24_outline
import moneymanagerkmp.composeapp.generated.resources.cd_add_expense
import moneymanagerkmp.composeapp.generated.resources.empty_home_action
import moneymanagerkmp.composeapp.generated.resources.empty_home_subtitle
import moneymanagerkmp.composeapp.generated.resources.empty_home_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@Composable
fun HomeScreenRoot(
    viewModel: HomeViewModel = koinViewModel(),
    onGotoAddScreen: () -> Unit = {},
    onGotoEditScreen: (Expense) -> Unit = {},
    navigationLayoutType: NavigationLayoutType = NavigationLayoutType.BOTTOM_NAVIGATION,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    var isShowExpenseLimitDialog by remember {
        mutableStateOf(false)
    }
    HomeScreen(
        state = state,
        onAction = { event ->
            when (event) {
                HomeAction.OnGotoAddScreen -> onGotoAddScreen()
                is HomeAction.OnGotoEditScreen -> {
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
    if (isShowExpenseLimitDialog) {
        SpendingLimitDialog(
            currentLimit = state.expenseLimit,
            currentWarnMid = state.warnMidPercent,
            currentWarnHigh = state.warnHighPercent,
            onDismissRequest = {
                isShowExpenseLimitDialog = false
            },
            onConfirmButtonClick = { limitText, warnMid, warnHigh ->
                viewModel.onAction(
                    HomeAction.OnSpendingLimitChange(
                        expenseLimitText = limitText,
                        warnMidPercent = warnMid,
                        warnHighPercent = warnHigh,
                    )
                )
            },
        )
    }
}

@Composable
fun HomeScreen(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
    isDebug: Boolean = false,
    navigationLayoutType: NavigationLayoutType = NavigationLayoutType.BOTTOM_NAVIGATION,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MoneyTheme.colors.bg,
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.size(MoneyTokens.FabSize),
                shape = RoundedCornerShape(MoneyTokens.FabRadius),
                containerColor = MoneyTheme.colors.accent,
                contentColor = MoneyTheme.colors.accentFg,
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 6.dp),
                onClick = {
                    onAction(HomeAction.OnGotoAddScreen)
                },
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(Res.string.cd_add_expense),
                )
            }
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            Spacer(modifier = Modifier.height(6.dp))
            DatePicker(
                year = state.nowDateYear.toIntOrNull() ?: 0,
                month = state.nowDateMonth.toIntOrNull() ?: 0,
                onDateChange = { year, month ->
                    onAction(
                        HomeAction.OnDatePick(
                            year = year,
                            month = month,
                        )
                    )
                },
            )
            Spacer(modifier = Modifier.height(10.dp))
            when (navigationLayoutType) {
                NavigationLayoutType.BOTTOM_NAVIGATION -> {
                    HomeScreenNaviBottom(
                        state = state,
                        onAction = onAction,
                        onExpenseLimitClick = {
                            onAction(HomeAction.OnExpenseLimitClick)
                        },
                    )
                }
                else -> {
                    HomeScreenNaviRail(
                        state = state,
                        onAction = onAction,
                        onExpenseLimitClick = {
                            onAction(HomeAction.OnExpenseLimitClick)
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeScreenNaviBottom(
    state: HomeState,
    onAction: (HomeAction) -> Unit = {},
    onExpenseLimitClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    LaunchedEffect(state.items.size) {
        if (state.items.isNotEmpty()) {
            listState.scrollToItem(0)
        }
    }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AmountTextLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MoneyTokens.ScreenHorizontalPadding),
            totalExpense = state.totalExpense,
            expenseLimit = state.expenseLimit,
            navigationLayoutType = NavigationLayoutType.BOTTOM_NAVIGATION,
            onExpenseLimitClick = onExpenseLimitClick,
            limitOverColor = state.limitOverColor,
        )
        Spacer(modifier = Modifier.height(14.dp))
        if (state.items.isEmpty()) {
            MoneyEmptyState(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MoneyTokens.ScreenHorizontalPadding),
                icon = painterResource(Res.drawable.baseline_receipt_24_outline),
                title = stringResource(Res.string.empty_home_title),
                subtitle = stringResource(Res.string.empty_home_subtitle),
                actionLabel = stringResource(Res.string.empty_home_action),
                onActionClick = { onAction(HomeAction.OnGotoAddScreen) },
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = MoneyTokens.ScreenHorizontalPadding),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                state = listState,
            ) {
                items(
                    items = state.items,
                    key = { it.first },
                ) { (_, expenses) ->
                    ExpenseItem(
                        modifier = Modifier.fillMaxWidth(),
                        items = expenses,
                        onItemClick = { expenseUi ->
                            onAction(HomeAction.OnGotoEditScreen(expenseUi = expenseUi))
                        },
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(88.dp))
                }
            }
        }
    }
}

@Composable
private fun HomeScreenNaviRail(
    state: HomeState,
    onAction: (HomeAction) -> Unit = {},
    onExpenseLimitClick: () -> Unit = {},
) {
    val listState = rememberLazyListState()
    LaunchedEffect(state.items.size) {
        if (state.items.isNotEmpty()) {
            listState.scrollToItem(0)
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MoneyTokens.ScreenHorizontalPadding),
    ) {
        AmountTextLayout(
            modifier = Modifier.width(IntrinsicSize.Max),
            totalExpense = state.totalExpense,
            expenseLimit = state.expenseLimit,
            navigationLayoutType = NavigationLayoutType.NAVIGATION_RAIL,
            onExpenseLimitClick = onExpenseLimitClick,
            limitOverColor = state.limitOverColor,
        )
        Spacer(modifier = Modifier.width(12.dp))
        if (state.items.isEmpty()) {
            MoneyEmptyState(
                modifier = Modifier.weight(1f),
                icon = painterResource(Res.drawable.baseline_receipt_24_outline),
                title = stringResource(Res.string.empty_home_title),
                subtitle = stringResource(Res.string.empty_home_subtitle),
                actionLabel = stringResource(Res.string.empty_home_action),
                onActionClick = { onAction(HomeAction.OnGotoAddScreen) },
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                state = listState,
            ) {
                items(
                    items = state.items,
                    key = { it.first },
                ) { (_, expenses) ->
                    ExpenseItem(
                        modifier = Modifier.fillMaxWidth(),
                        items = expenses,
                        onItemClick = { expenseUi ->
                            onAction(HomeAction.OnGotoEditScreen(expenseUi = expenseUi))
                        },
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(88.dp))
                }
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    AppTheme(darkTheme = false) {
        HomeScreen(
            state = HomeState(
                totalExpense = 11460,
                expenseLimit = 30000,
                nowDateYear = "2026",
                nowDateMonth = "11",
                nowDateDayOfMonth = "21",
            ),
            onAction = {},
            isDebug = true,
        )
    }
}

@Preview
@Composable
private fun HomeScreenDarkPreview() {
    AppTheme(darkTheme = true) {
        HomeScreen(
            state = HomeState(
                totalExpense = 24000,
                expenseLimit = 30000,
                nowDateYear = "2026",
                nowDateMonth = "11",
                nowDateDayOfMonth = "21",
            ),
            onAction = {},
            isDebug = true,
        )
    }
}
