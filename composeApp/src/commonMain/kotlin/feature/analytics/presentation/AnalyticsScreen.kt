@file:OptIn(KoinExperimentalAPI::class, ExperimentalMaterial3Api::class)

package feature.analytics.presentation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.presentation.LocalDarkLightMode
import core.presentation.components.DatePicker
import feature.analytics.presentation.components.LineChart
import feature.analytics.presentation.components.TextChip
import feature.analytics.presentation.model.LineChartStyle
import getScreenWidth
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.backup_backup_icon
import moneymanagerkmp.composeapp.generated.resources.expense
import moneymanagerkmp.composeapp.generated.resources.outline_light_mode_24
import moneymanagerkmp.composeapp.generated.resources.outline_night_mode_24
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import toMoneyString

@Composable
fun AnalyticsScreenRoot(
    viewModel: AnalyticsViewModel = koinViewModel(),
    onBackupClick: () -> Unit = {},
    onDarkLightModeSwitch: () -> Unit = {}
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    AnalyticsScreen(
        state = state,
        onEvent = { event ->
            when(event){
                AnalyticsEvent.OnBackupClick         -> onBackupClick()
                AnalyticsEvent.OnDarkLightModeSwitch -> onDarkLightModeSwitch()
                else -> Unit
            }
            viewModel.onEvent(event)
        }
    )

}

@Composable
fun AnalyticsScreen(
    state: AnalyticsState,
    onEvent: (AnalyticsEvent) -> Unit = {},
) {

    val chartLineColor = MaterialTheme.colorScheme.onBackground
    val unselectedColor = MaterialTheme.colorScheme.onSecondaryContainer
    val isDarkMode = LocalDarkLightMode.current
    val lineChartStyle = remember(isDarkMode){
        LineChartStyle(
            chartLineColor = chartLineColor,
            unselectedColor = unselectedColor,
            selectedColor =chartLineColor,
            helperLinesThicknessPx = 1f,
            axisLinesThicknessPx = 5f,
            labelFontSize = 14.sp,
            minYLabelSpacing = 25.dp,
            verticalPadding = 12.dp,
            horizontalPadding = 8.dp,
            xAxisLabelSpacing = 16.dp
        )
    }
    val expenseLineChartProgress = remember(state.dateFilter) {
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    IconButton(
                        onClick = {
                            onEvent(AnalyticsEvent.OnDarkLightModeSwitch)
                        }
                    ){
                        Icon(
                            painter = painterResource(
                                if (isDarkMode){
                                    Res.drawable.outline_light_mode_24
                                }else{
                                    Res.drawable.outline_night_mode_24
                                }
                            ),
                            contentDescription = "backup icon"
                        )
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
            )
        }
    ){
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
                ,
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
            if (state.dateFilter == DateFilter.ONE_MONTH){
                DatePicker(
                    year = state.nowDateYear.toIntOrNull()?:0,
                    month = state.nowDateMonth.toIntOrNull()?:0,
                    onDateChange = { year , month ->
                        onEvent(
                            AnalyticsEvent.OnDatePick(
                                year = year,
                                month = month
                            )
                        )
                    }
                )
            }
            if (state.expenseSum != 0L){
                ExpenseSection(
                    state = state,
                    lineChartStyle = lineChartStyle,
                    expenseLineChartProgress = expenseLineChartProgress,
                    onEvent = onEvent
                )
            }
        }

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

