package feature.chart.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import feature.chart.presentation.components.ChartLayout
import feature.core.navigation.CustomTab
import feature.core.navigation.CustomTabOptions
import feature.core.presentation.components.DatePicker
import feature.home.presentation.HomeEvent
import format
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.baseline_pie_chart_24_filled
import moneymanagerkmp.composeapp.generated.resources.baseline_pie_chart_24_outline
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

object ChartTab : CustomTab {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val chartScreenModel = getScreenModel<ChartScreenModel>()
        val state by chartScreenModel.state.collectAsState()
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
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 8.dp)
            ){
                if (state.items.isNotEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 20.dp)
                        ) {
                            ChartLayout(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                items = state.items,
                                isIncomeShow = state.isIncomeShow,
                                onChartClick = {
                                     chartScreenModel.onEvent(
                                         ChartEvent.OnChartClick
                                     )
                                },
                                detailExpense = state.expensesTypeList
                            )

                        }
                    }
                }
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