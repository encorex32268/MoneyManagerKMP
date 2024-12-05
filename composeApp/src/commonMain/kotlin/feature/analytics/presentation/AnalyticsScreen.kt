@file:OptIn(KoinExperimentalAPI::class)

package feature.analytics.presentation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import feature.analytics.presentation.components.FilterChip
import feature.analytics.presentation.model.DataPoint
import feature.analytics.presentation.model.LineChartStyle
import feature.core.presentation.Texts
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
    onBackupClick: () -> Unit = {}
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
        }
    )

}

@Composable
private fun AnalyticsScreen(
    state: AnalyticsState,
    onEvent: (AnalyticsEvent) -> Unit = {}
) {
    val animationProgress = remember(state.dateFilter) {
        Animatable(0f)
    }

    val animationProgress2 = remember(state.dateFilter) {
        Animatable(0f)
    }
    LaunchedEffect(key1 = state) {
        animationProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1500,
                easing = LinearOutSlowInEasing
            )
        )

    }
    LaunchedEffect(key1 = state){
        animationProgress2.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1500,
                easing = LinearOutSlowInEasing
            )
        )
    }


    Column (
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp,Alignment.CenterVertically),
        horizontalAlignment = Alignment.Start
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.1f)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Row(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ){
                DateFilter.entries.forEach { dateFilter ->
                    FilterChip(
                        modifier = Modifier.size(50.dp),
                        dateFilter = dateFilter,
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

        Row(
            Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Texts.TitleMedium(
                text = stringResource(Res.string.expense)
            )
            Texts.TitleLarge(
                modifier = Modifier.padding(start = 4.dp),
                text = state.expenseSum.toMoneyString()
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.4f)
                .horizontalScroll(rememberScrollState())
            ,
        ){
            LineChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .width((getScreenWidth().value * 10) .dp ),
                dataPoints = state.dataPoints,
                style = LineChartStyle(
                    chartLineColor = Color.Black,
                    unselectedColor = Color(0xFF7C7C7C),
                    selectedColor = Color.Black,
                    helperLinesThicknessPx = 1f,
                    axisLinesThicknessPx = 5f,
                    labelFontSize = 14.sp,
                    minYLabelSpacing = 25.dp,
                    verticalPadding = 16.dp,
                    horizontalPadding = 8.dp,
                    xAxisLabelSpacing = 16.dp
                ),
                animationProgress = animationProgress.value,
                selectedDataPoint = state.selectedDataPoint,
                onSelectedDataPoint = {
                    onEvent(AnalyticsEvent.OnSelectDataPoint(it))
                }

            )

        }

        Row(
            Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Texts.TitleMedium(
                text = stringResource(Res.string.income)
            )
            Texts.TitleLarge(
                modifier = Modifier.padding(start = 4.dp),
                text = state.incomeSum.toMoneyString()
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.4f)
                .horizontalScroll(rememberScrollState())
            ,
        ){
            LineChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .width((getScreenWidth().value * 10) .dp ),
                dataPoints = state.incomeDataPoints,
                style = LineChartStyle(
                    chartLineColor = Color.Black,
                    unselectedColor = Color(0xFF7C7C7C),
                    selectedColor = Color.Black,
                    helperLinesThicknessPx = 1f,
                    axisLinesThicknessPx = 5f,
                    labelFontSize = 14.sp,
                    minYLabelSpacing = 25.dp,
                    verticalPadding = 16.dp,
                    horizontalPadding = 8.dp,
                    xAxisLabelSpacing = 16.dp
                ),
                animationProgress = animationProgress2.value,
                selectedDataPoint = state.incomeSelectedDataPoint,
                onSelectedDataPoint = {
                    onEvent(AnalyticsEvent.OnIncomeSelectDataPoint(it))
                }
            )

        }

    }


}

