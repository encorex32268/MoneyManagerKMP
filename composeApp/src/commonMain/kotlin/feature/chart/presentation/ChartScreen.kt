@file:OptIn(ExperimentalResourceApi::class, ExperimentalFoundationApi::class,
    KoinExperimentalAPI::class
)

package feature.chart.presentation

import AdMobBannerController
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import feature.core.presentation.components.DatePicker

import feature.chart.presentation.components.ChartLayout
import feature.chart.presentation.components.ExpenseDetailLazyGrid
import feature.core.domain.model.Expense
import feature.core.domain.model.Type
import feature.core.presentation.customTabIndicatorOffset
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.expense
import moneymanagerkmp.composeapp.generated.resources.income
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

private const val EXPENSE = 0
private const val INCOME = 1
private const val TAB_SIZE = 2

@Composable
fun ChartScreenRoot(
    viewModel: ChartViewModel = koinViewModel(),
    onGotoDetail: (List<Expense>,Type) -> Unit = { _ , _->}
){

    val state by viewModel.state.collectAsStateWithLifecycle()
    ChartScreen(
        state = state,
        onEvent = { event ->
            when(event){
                is ChartEvent.OnGotoDetail ->{
                    onGotoDetail(event.expenses , event.type)
                }
                else -> Unit
            }
            viewModel.onEvent(event)
        }
    )
}

@Composable
fun ChartScreen(
    state: ChartState,
    onEvent: (ChartEvent) -> Unit = {},
    isDebug: Boolean = false
){
    val density = LocalDensity.current
    val tabWidths = remember {
        val tabWidthStateList = mutableStateListOf<Dp>()
        repeat(TAB_SIZE){
            tabWidthStateList.add(0.dp)
        }
        tabWidthStateList
    }
    val selectTabIndex = remember(state) {
        if(state.isIncomeShown) INCOME else EXPENSE
    }

    val sumTotal = remember(state) {
        var sum = 0L
        if (state.isIncomeShown){
            state.items.forEach { chart ->
                chart.itemsIncome.forEach {
                    sum += it.cost
                }
            }
        }else{
            state.items.forEach {chart ->
                chart.itemsNotIncome.forEach {
                    sum += it.cost
                }
            }
        }
        sum
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        DatePicker(
            year = state.nowDateYear.toIntOrNull()?:0,
            month = state.nowDateMonth.toIntOrNull()?:0,
            onDateChange = { year , month ->
                onEvent(
                    ChartEvent.OnDatePick(
                        year = year,
                        month = month
                    )
                )
            }
        )
        TabRow(
            selectedTabIndex = selectTabIndex,
            containerColor = Color.White,
            contentColor = Color.Black,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.customTabIndicatorOffset(
                        currentTabPosition = tabPositions[selectTabIndex],
                        tabWidth = tabWidths[selectTabIndex]
                    ),
                    height = 2.dp
                )
            },
            tabs = {
                Tab(
                    selected = !state.isIncomeShown,
                    onClick = {
                        onEvent(
                            ChartEvent.OnTypeChange(false)
                        )
                    },
                    text = {
                        Text(
                            onTextLayout = {
                                tabWidths[0] = with(
                                    density
                                ){
                                    it.size.width.toDp() + 10.dp
                                }
                            },
                            text = stringResource(Res.string.expense),
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                )
                Tab(
                    selected = state.isIncomeShown,
                    onClick = {
                        onEvent(
                            ChartEvent.OnTypeChange(true)
                        )
                    },
                    text = {
                        Text(
                            onTextLayout = {
                                tabWidths[1] = with(
                                    density
                                ){
                                    it.size.width.toDp() + 10.dp
                                }
                            },
                            text = stringResource(Res.string.income),
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                )
            }
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ){
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                ChartLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    state = state,
                    sumTotal = sumTotal
                )
                Spacer(Modifier.height(8.dp))
                ExpenseDetailLazyGrid(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                    ,
                    items = if (state.isIncomeShown) state.incomeItems else state.items,
                    isIncomeShown = state.isIncomeShown,
                    sumTotal = sumTotal,
                    onItemClick = {
                        onEvent(
                            ChartEvent.OnGotoDetail(
                                expenses = if (state.isIncomeShown){
                                    it.itemsIncome
                                }else {
                                    it.itemsNotIncome
                                },
                                type = it.type
                            )
                        )
                    }
                )
            }
            if (!isDebug){
                AdMobBannerController.AdMobBannerCompose(
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

}