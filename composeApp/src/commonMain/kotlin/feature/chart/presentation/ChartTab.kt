package feature.chart.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import feature.chart.presentation.chartdetail.ChartDetailScreen
import feature.chart.presentation.components.ChartLayout
import feature.chart.presentation.components.ExpenseDetailLayout
import feature.core.navigation.CustomTab
import feature.core.navigation.CustomTabOptions
import feature.core.presentation.components.DatePicker
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.baseline_pie_chart_24_filled
import moneymanagerkmp.composeapp.generated.resources.baseline_pie_chart_24_outline
import moneymanagerkmp.composeapp.generated.resources.expense
import moneymanagerkmp.composeapp.generated.resources.income
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

object ChartTab : CustomTab {

    private const val EXPENSE = 0
    private const val INCOME = 1

    @ExperimentalResourceApi
    @ExperimentalFoundationApi
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val chartScreenModel = getScreenModel<ChartScreenModel>()
        val state by chartScreenModel.state.collectAsState()
        val selectTabIndex by remember {
            derivedStateOf {
                if(state.isIncomeShown) INCOME else EXPENSE
            }
        }
        LaunchedEffect(Unit){
            chartScreenModel.onEvent(
                ChartEvent.OnDatePick()
            )
        }
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            DatePicker(
                year = state.nowDateYear.toIntOrNull()?:0,
                month = state.nowDateMonth.toIntOrNull()?:0,
                onDateChange = { year , month ->
                    chartScreenModel.onEvent(
                        ChartEvent.OnDatePick(
                            year = year,
                            month = month
                        )
                    )
                }
            )

            //TODO: TabRow?
            androidx.compose.material3.TabRow(
                selectedTabIndex = selectTabIndex,
                contentColor = Color.Black,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        color = Color.Black,
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectTabIndex])
                    )
                },
                tabs = {
                    Tab(
                        selected = !state.isIncomeShown,
                        onClick = {
                            chartScreenModel.onEvent(
                                ChartEvent.OnTypeChange(false)
                            )
                        },
                        text = {
                            Text(
                                text = stringResource(Res.string.expense)
                            )
                        }
                    )
                    Tab(
                        selected = state.isIncomeShown,
                        onClick = {
                            chartScreenModel.onEvent(
                                ChartEvent.OnTypeChange(true)
                            )
                        },
                        text = {
                            Text(
                                text = stringResource(Res.string.income)
                            )
                        }
                    )
                }
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                ChartLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    items = if (state.isIncomeShown)
                        state.incomeTypeList
                    else state.expenseTypeList,
                    sumTotal = kotlin.run {
                        val items = if (state.isIncomeShown)
                            state.incomeTypeList
                        else state.expenseTypeList

                        var sum = 0L
                        items.forEach {
                            it.expenseItems.forEach {
                                sum += it.cost
                            }
                        }
                        sum
                    }
                )
                Spacer(Modifier.height(8.dp))
                ExpenseDetailLayout(
                    modifier = Modifier
                        .fillMaxWidth(),
                    items = if (state.isIncomeShown)
                        state.incomeTypeList
                    else state.expenseTypeList,
                    sumTotal = kotlin.run {
                        val items = if (state.isIncomeShown)
                            state.incomeTypeList
                        else state.expenseTypeList

                        var sum = 0L
                        items.forEach {
                            it.expenseItems.forEach {
                                sum += it.cost
                            }
                        }
                        sum
                    },
                    onItemClick = {
                        navigator.parent?.push(
                            ChartDetailScreen(
                                items = it.expenseItems
                            )
                        )
                    }
                )

            }
        }

    }

    @OptIn(ExperimentalResourceApi::class)
    override val customTabOptions: CustomTabOptions
        @Composable
        get() {
            val selectedIcon = painterResource(Res.drawable.baseline_pie_chart_24_filled)
            val unSelectedIcon = painterResource(Res.drawable.baseline_pie_chart_24_outline)
            return remember{
                CustomTabOptions(
                    index = 1u,
                    title = "Chart",
                    selectedIcon = selectedIcon,
                    unSelectedIcon = unSelectedIcon
                )
            }
        }
}