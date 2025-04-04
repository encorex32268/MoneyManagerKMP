@file:OptIn(ExperimentalResourceApi::class, ExperimentalFoundationApi::class,
    KoinExperimentalAPI::class
)

package feature.chart.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import feature.chart.presentation.components.ChartLayout
import feature.chart.presentation.components.ExpenseDetailLazyGrid
import core.domain.model.Expense
import core.domain.model.Type
import core.presentation.components.DatePicker
import core.presentation.components.SpendingLimitsProgressBar
import core.presentation.navigation.NavigationLayoutType
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI


@Composable
fun ChartScreenRoot(
    viewModel: ChartViewModel = koinViewModel(),
    onGotoDetail: (List<Expense>,Type) -> Unit = { _ , _->},
    navigationLayoutType: NavigationLayoutType = NavigationLayoutType.BOTTOM_NAVIGATION
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
        },
        navigationLayoutType = navigationLayoutType
    )
}

@Composable
fun ChartScreen(
    state: ChartState,
    onEvent: (ChartEvent) -> Unit = {},
    isDebug: Boolean = false,
    navigationLayoutType: NavigationLayoutType = NavigationLayoutType.BOTTOM_NAVIGATION
){

    Column(
        modifier = Modifier
            .fillMaxSize()
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
        SpendingLimitsProgressBar(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            spendingLimit = state.spendingLimit,
            totalExpense = state.totalExpense
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalArrangement = Arrangement.Center
        ){
            ChartDataSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .weight(1f),
                navigationLayoutType = navigationLayoutType,
                content = {
                    ChartLayout(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                            .weight(0.5f),
                        state = state,
                    )
                    if (navigationLayoutType == NavigationLayoutType.BOTTOM_NAVIGATION){
                        Spacer(Modifier.height(8.dp))
                    }else{
                        Spacer(Modifier.width(8.dp))
                    }
                    ExpenseDetailLazyGrid(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.5f),
                        items = state.items,
                        sumTotal = state.totalExpense,
                        onItemClick = {
                            onEvent(
                                ChartEvent.OnGotoDetail(
                                    expenses = it.items,
                                    type = it.type
                                )
                            )
                        }
                    )
                }
            )

        }

    }

}

@Composable
private fun ChartDataSection(
    modifier: Modifier = Modifier,
    navigationLayoutType: NavigationLayoutType,
    content: @Composable () -> Unit = {}
){
    when(navigationLayoutType){
        NavigationLayoutType.BOTTOM_NAVIGATION -> {
            Column(
                modifier = modifier
            ){
                content()
            }
        }
        else -> {
            Row(
                modifier = modifier
            ){
                content()
            }
        }
    }
}


