@file:OptIn(ExperimentalResourceApi::class, ExperimentalFoundationApi::class,
    KoinExperimentalAPI::class
)

package feature.chart

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import feature.core.presentation.components.DatePicker

import feature.chart.components.ChartLayout
import feature.chart.components.ExpenseDetailLayout
import feature.core.domain.model.chart.Chart
import feature.presentation.customTabIndicatorOffset
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.expense
import moneymanagerkmp.composeapp.generated.resources.income
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

private const val EXPENSE = 0
private const val INCOME = 1

@Composable
fun ChartScreenRoot(
    viewModel: ChartViewModel = koinViewModel(),
    onGotoDetail: (Chart) -> Unit = {}
){

    val state by viewModel.state.collectAsState()
    LaunchedEffect(Unit){
        viewModel.onEvent(
            ChartEvent.OnDatePick()
        )
    }
    ChartScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onGotoDetail = onGotoDetail
    )
}

@Composable
fun ChartScreen(
    state: ChartState,
    onEvent: (ChartEvent) -> Unit = {},
    onGotoDetail: (Chart) -> Unit = {}
){
    val density = LocalDensity.current
    val tabWidths = remember {
        val tabWidthStateList = mutableStateListOf<Dp>()
        repeat(2){
            tabWidthStateList.add(0.dp)
        }
        tabWidthStateList
    }
    val selectTabIndex = remember(state) {
        if(state.isIncomeShown) INCOME else EXPENSE
    }

    val items = remember(state) {
        if (state.isIncomeShown)
            state.incomeTypeList
        else state.expenseTypeList
    }
    val sumTotal = remember(state) {
        run {
            var sum = 0L
            items.forEach {
                it.expenseItems.forEach {
                    sum += it.cost
                }
            }
            sum
        }
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
                            text = stringResource(Res.string.expense)
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
                items = items,
                sumTotal = sumTotal
            )
            Spacer(Modifier.height(8.dp))
            ExpenseDetailLayout(
                modifier = Modifier
                    .fillMaxWidth(),
                items = items,
                sumTotal = sumTotal,
                onItemClick = onGotoDetail
            )

        }
    }

}