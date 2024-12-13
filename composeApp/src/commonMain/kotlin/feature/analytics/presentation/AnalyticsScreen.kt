@file:OptIn(KoinExperimentalAPI::class)

package feature.analytics.presentation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import feature.analytics.presentation.components.LineChart
import feature.analytics.presentation.components.TextChip
import feature.analytics.presentation.model.LineChartStyle
import feature.core.presentation.navigation.NavigationLayoutType
import getScreenWidth
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.backup_backup_icon
import moneymanagerkmp.composeapp.generated.resources.expense
import moneymanagerkmp.composeapp.generated.resources.income
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import toMoneyString

@Composable
fun AnalyticsScreenRoot(
    viewModel: AnalyticsViewModel = koinViewModel(),
    onBackupClick: () -> Unit = {},
    navigationLayoutType: NavigationLayoutType = NavigationLayoutType.BOTTOM_NAVIGATION
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    AnalyticsScreen(
        state = state,
        onEvent = { event ->
            when(event){
                AnalyticsEvent.OnBackupClick         -> onBackupClick()
                else -> Unit
            }
            viewModel.onEvent(event)
        },
        navigationLayoutType = navigationLayoutType
    )

}

@Composable
private fun AnalyticsScreen(
    state: AnalyticsState,
    onEvent: (AnalyticsEvent) -> Unit = {},
    navigationLayoutType: NavigationLayoutType = NavigationLayoutType.BOTTOM_NAVIGATION
) {

    val lineChartStyle = remember {
        LineChartStyle(
            chartLineColor = Color.Black,
            unselectedColor = Color(0xFFC8C8C8),
            selectedColor = Color.Black,
            helperLinesThicknessPx = 1f,
            axisLinesThicknessPx = 5f,
            labelFontSize = 14.sp,
            minYLabelSpacing = 25.dp,
            verticalPadding = 12.dp,
            horizontalPadding = 8.dp,
            xAxisLabelSpacing = 16.dp
        )
    }
    val expenseLineChartProgress = remember(state.dateFilter , state.moneyManagerTypeFilter) {
        Animatable(0f)
    }

    val incomeLineChartProgress = remember(state.dateFilter , state.moneyManagerTypeFilter) {
        Animatable(0f)
    }
    LaunchedEffect(key1 = state) {
        expenseLineChartProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1500,
                easing = LinearOutSlowInEasing
            )
        )

    }
    LaunchedEffect(key1 = state){
        incomeLineChartProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1500,
                easing = LinearOutSlowInEasing
            )
        )
    }


    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
        ,
        verticalArrangement = Arrangement.spacedBy(8.dp,Alignment.CenterVertically),
        horizontalAlignment = Alignment.Start
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.1f)
                .padding(vertical = 8.dp)
                .padding(start = 16.dp)
            ,
            verticalAlignment = Alignment.CenterVertically
        ){
            Row(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ){
                DateFilter.entries.forEach { dateFilter ->
                    TextChip(
                        modifier = Modifier.size(36.dp),
                        text = dateFilter.text,
                        onClick = {
                            onEvent(AnalyticsEvent.OnDateFilterChange(dateFilter))
                        },
                        isSelected = state.dateFilter.text == dateFilter.text
                    )
                }
            }
            IconButton(
                onClick = {
                    onEvent(AnalyticsEvent.OnBackupClick)
                }
            ){
                Icon(
                    painter = painterResource(Res.drawable.backup_backup_icon),
                    contentDescription = "backup icon"
                )
            }
        }
        when(navigationLayoutType){
            NavigationLayoutType.BOTTOM_NAVIGATION -> {
                if (state.expenseSum != 0L){
                    ExpenseSection(
                        state = state,
                        lineChartStyle = lineChartStyle,
                        expenseLineChartProgress = expenseLineChartProgress,
                        onEvent = onEvent
                    )
                }
                if (state.incomeSum != 0L){
                    IncomeSection(
                        state = state,
                        lineChartStyle = lineChartStyle,
                        incomeLineChartProgress = incomeLineChartProgress,
                        onEvent = onEvent
                    )
                }
            }
            else -> {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(start = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ){
                    MoneyManagerTypeFilter.entries.forEach { moneymanagerType ->
                        TextChip(
                            modifier = Modifier.size(36.dp),
                            text = when(moneymanagerType){
                                MoneyManagerTypeFilter.EXPENSE -> stringResource(Res.string.expense)
                                else ->  stringResource(Res.string.income)
                            },
                            onClick = {
                                onEvent(AnalyticsEvent.OnMoneyManagerTypeChange(moneymanagerType))
                            },
                            isSelected = state.moneyManagerTypeFilter == moneymanagerType
                        )
                    }
                }
                when(state.moneyManagerTypeFilter){
                    MoneyManagerTypeFilter.EXPENSE  -> {
                        if (state.expenseSum != 0L){
                            ExpenseSection(
                                state = state,
                                lineChartStyle = lineChartStyle,
                                expenseLineChartProgress = expenseLineChartProgress,
                                onEvent = onEvent
                            )
                        }
                    }
                    MoneyManagerTypeFilter.INCOME -> {
                        if (state.incomeSum != 0L){
                            IncomeSection(
                                state = state,
                                lineChartStyle = lineChartStyle,
                                incomeLineChartProgress = incomeLineChartProgress,
                                onEvent = onEvent
                            )
                        }
                    }
                }
            }
        }


    }


}

@Composable
private fun ColumnScope.IncomeSection(
    state: AnalyticsState,
    lineChartStyle: LineChartStyle,
    incomeLineChartProgress: Animatable<Float, AnimationVector1D>,
    onEvent: (AnalyticsEvent) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = stringResource(Res.string.income),
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            modifier = Modifier.padding(start = 4.dp),
            text = state.incomeSum.toMoneyString(),
            style = MaterialTheme.typography.titleSmall.copy(
                fontSize = 20.sp
            )
        )
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .weight(0.4f)
            .horizontalScroll(rememberScrollState()),
    ) {
        LineChart(
            modifier = Modifier
                .fillMaxWidth()
                .width((getScreenWidth().value * 10).dp),
            dataPoints = state.incomeDataPoints,
            style = lineChartStyle,
            animationProgress = incomeLineChartProgress.value,
            selectedDataPoint = state.incomeSelectedDataPoint,
            onSelectedDataPoint = {
                onEvent(AnalyticsEvent.OnIncomeSelectDataPoint(it))
            }
        )

    }
}

@Composable
private fun ColumnScope.ExpenseSection(
    state: AnalyticsState,
    lineChartStyle: LineChartStyle,
    expenseLineChartProgress: Animatable<Float, AnimationVector1D>,
    onEvent: (AnalyticsEvent) -> Unit
) {
    Row(
        Modifier.fillMaxWidth().padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(Res.string.expense),
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            modifier = Modifier.padding(start = 4.dp),
            text = state.expenseSum.toMoneyString(),
            style = MaterialTheme.typography.titleSmall.copy(
                fontSize = 20.sp
            )
        )

    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .weight(0.4f)
            .horizontalScroll(rememberScrollState()),
    ) {

        LineChart(
            modifier = Modifier
                .fillMaxWidth()
                .width((getScreenWidth().value * 10).dp),
            dataPoints = state.dataPoints,
            style = lineChartStyle,
            animationProgress = expenseLineChartProgress.value,
            selectedDataPoint = state.selectedDataPoint,
            onSelectedDataPoint = {
                onEvent(AnalyticsEvent.OnSelectDataPoint(it))
            }
        )

    }
}

