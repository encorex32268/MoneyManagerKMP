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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import core.presentation.components.MoneyCard
import core.presentation.components.MoneyEmptyState
import core.ui.MoneyTheme
import core.ui.MoneyTokens
import feature.analytics.presentation.components.LineChart
import feature.analytics.presentation.components.TextChip
import feature.analytics.presentation.model.LineChartStyle
import getScreenWidth
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.analytics_daily_avg
import moneymanagerkmp.composeapp.generated.resources.analytics_title
import moneymanagerkmp.composeapp.generated.resources.backup_backup_icon
import moneymanagerkmp.composeapp.generated.resources.baseline_analytics_24_outline
import moneymanagerkmp.composeapp.generated.resources.cd_backup
import moneymanagerkmp.composeapp.generated.resources.cd_toggle_theme
import moneymanagerkmp.composeapp.generated.resources.empty_analytics_subtitle
import moneymanagerkmp.composeapp.generated.resources.empty_analytics_title
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
    onDarkLightModeSwitch: () -> Unit = {},
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    AnalyticsScreen(
        state = state,
        onAction = { event ->
            when (event) {
                AnalyticsAction.OnBackupClick -> onBackupClick()
                AnalyticsAction.OnDarkLightModeSwitch -> onDarkLightModeSwitch()
                else -> Unit
            }
            viewModel.onAction(event)
        },
    )
}

@Composable
fun AnalyticsScreen(
    state: AnalyticsState,
    onAction: (AnalyticsAction) -> Unit = {},
) {
    val chartLineColor = MoneyTheme.colors.ink
    val unselectedColor = MoneyTheme.colors.muted
    val isDarkMode = LocalDarkLightMode.current
    val lineChartStyle = remember(isDarkMode, chartLineColor, unselectedColor) {
        LineChartStyle(
            chartLineColor = chartLineColor,
            unselectedColor = unselectedColor,
            selectedColor = chartLineColor,
            helperLinesThicknessPx = 1f,
            axisLinesThicknessPx = 3f,
            labelFontSize = 12.sp,
            minYLabelSpacing = 25.dp,
            verticalPadding = 12.dp,
            horizontalPadding = 8.dp,
            xAxisLabelSpacing = 16.dp,
        )
    }
    val expenseLineChartProgress = remember(state.dateFilter) {
        Animatable(0f)
    }
    LaunchedEffect(key1 = state) {
        expenseLineChartProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1200,
                easing = LinearOutSlowInEasing,
            ),
        )
    }

    Scaffold(
        containerColor = MoneyTheme.colors.bg,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MoneyTheme.colors.bg,
                    titleContentColor = MoneyTheme.colors.ink,
                    actionIconContentColor = MoneyTheme.colors.ink,
                ),
                title = {
                    Text(
                        text = stringResource(Res.string.analytics_title),
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                actions = {
                    IconButton(onClick = { onAction(AnalyticsAction.OnDarkLightModeSwitch) }) {
                        Icon(
                            painter = painterResource(
                                if (isDarkMode) {
                                    Res.drawable.outline_light_mode_24
                                } else {
                                    Res.drawable.outline_night_mode_24
                                }
                            ),
                            contentDescription = stringResource(Res.string.cd_toggle_theme),
                        )
                    }
                    IconButton(onClick = { onAction(AnalyticsAction.OnBackupClick) }) {
                        Icon(
                            painter = painterResource(Res.drawable.backup_backup_icon),
                            contentDescription = stringResource(Res.string.cd_backup),
                        )
                    }
                },
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MoneyTokens.ScreenHorizontalPadding),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                DateFilter.entries.forEach { dateFilter ->
                    TextChip(
                        text = dateFilter.text,
                        onClick = { onAction(AnalyticsAction.OnDateFilterChange(dateFilter)) },
                        isSelected = state.dateFilter.text == dateFilter.text,
                    )
                }
            }
            if (state.dateFilter == DateFilter.ONE_MONTH) {
                DatePicker(
                    year = state.nowDateYear.toIntOrNull() ?: 0,
                    month = state.nowDateMonth.toIntOrNull() ?: 0,
                    onDateChange = { year, month ->
                        onAction(AnalyticsAction.OnDatePick(year = year, month = month))
                    },
                )
            }
            if (state.expenseSum != 0L) {
                ExpenseSection(
                    state = state,
                    lineChartStyle = lineChartStyle,
                    expenseLineChartProgress = expenseLineChartProgress,
                    onAction = onAction,
                )
            } else {
                MoneyEmptyState(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MoneyTokens.ScreenHorizontalPadding),
                    icon = painterResource(Res.drawable.baseline_analytics_24_outline),
                    title = stringResource(Res.string.empty_analytics_title),
                    subtitle = stringResource(Res.string.empty_analytics_subtitle),
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
    onAction: (AnalyticsAction) -> Unit,
) {
    val avgPerPoint = remember(state.expenseSum, state.dataPoints.size) {
        if (state.dataPoints.isNotEmpty()) state.expenseSum / state.dataPoints.size else 0L
    }

    MoneyCard(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f)
            .padding(horizontal = MoneyTokens.ScreenHorizontalPadding)
            .padding(bottom = 16.dp),
        contentPadding = PaddingValues(18.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
        ) {
            Column {
                Text(
                    text = "${stringResource(Res.string.expense)} (${state.dateFilter.text})",
                    style = MaterialTheme.typography.labelMedium,
                    color = MoneyTheme.colors.muted,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = state.expenseSum.toMoneyString(),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MoneyTheme.colors.ink,
                )
            }
            if (avgPerPoint > 0L) {
                Text(
                    text = stringResource(Res.string.analytics_daily_avg, avgPerPoint.toMoneyString()),
                    style = MaterialTheme.typography.bodySmall,
                    color = MoneyTheme.colors.muted,
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
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
                    onAction(AnalyticsAction.OnSelectDataPoint(it))
                },
            )
        }
    }
}

@org.jetbrains.compose.ui.tooling.preview.Preview
@Composable
private fun AnalyticsScreenPreview() {
    app.presentation.AppTheme {
        AnalyticsScreen(
            state = AnalyticsState(
                nowDateYear = "2026",
                nowDateMonth = "11",
                expenseSum = 11460L,
            )
        )
    }
}
