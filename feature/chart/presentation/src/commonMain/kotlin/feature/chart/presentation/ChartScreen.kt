@file:OptIn(
    ExperimentalResourceApi::class,
    ExperimentalFoundationApi::class,
    KoinExperimentalAPI::class,
)

package feature.chart.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import core.domain.model.Expense
import core.domain.model.Type
import core.presentation.components.DatePicker
import core.presentation.components.MoneyCard
import core.presentation.components.MoneyEmptyState
import core.presentation.navigation.NavigationLayoutType
import core.ui.MoneyTheme
import core.ui.MoneyTokens
import feature.chart.presentation.components.ChartLayout
import feature.chart.presentation.components.ExpenseDetailLazyGrid
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.baseline_pie_chart_24_outline
import moneymanagerkmp.composeapp.generated.resources.empty_chart_subtitle
import moneymanagerkmp.composeapp.generated.resources.empty_chart_title
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@Composable
fun ChartScreenRoot(
    viewModel: ChartViewModel = koinViewModel(),
    onGotoDetail: (List<Expense>, Type) -> Unit = { _, _ -> },
    navigationLayoutType: NavigationLayoutType = NavigationLayoutType.BOTTOM_NAVIGATION,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ChartScreen(
        state = state,
        onAction = { event ->
            when (event) {
                is ChartAction.OnGotoDetail -> onGotoDetail(event.expenses, event.type)
                else -> Unit
            }
            viewModel.onAction(event)
        },
        navigationLayoutType = navigationLayoutType,
    )
}

@Composable
fun ChartScreen(
    state: ChartState,
    onAction: (ChartAction) -> Unit = {},
    isDebug: Boolean = false,
    navigationLayoutType: NavigationLayoutType = NavigationLayoutType.BOTTOM_NAVIGATION,
) {
    val isEmpty = state.totalExpense == 0L || state.items.isEmpty()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MoneyTheme.colors.bg),
    ) {
        Spacer(modifier = Modifier.height(6.dp))
        DatePicker(
            year = state.nowDateYear.toIntOrNull() ?: 0,
            month = state.nowDateMonth.toIntOrNull() ?: 0,
            onDateChange = { year, month ->
                onAction(ChartAction.OnDatePick(year = year, month = month))
            },
        )
        Spacer(modifier = Modifier.height(10.dp))

        if (isEmpty) {
            MoneyEmptyState(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MoneyTokens.ScreenHorizontalPadding),
                icon = painterResource(Res.drawable.baseline_pie_chart_24_outline),
                title = stringResource(Res.string.empty_chart_title),
                subtitle = stringResource(Res.string.empty_chart_subtitle),
            )
        } else {
            when (navigationLayoutType) {
                NavigationLayoutType.BOTTOM_NAVIGATION -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = MoneyTokens.ScreenHorizontalPadding),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                    ) {
                        MoneyCard(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(18.dp),
                        ) {
                            ChartLayout(
                                modifier = Modifier.fillMaxWidth(),
                                state = state,
                            )
                        }

                        MoneyCard(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                        ) {
                            ExpenseDetailLazyGrid(
                                modifier = Modifier.fillMaxWidth(),
                                items = state.items,
                                sumTotal = state.totalExpense,
                                onItemClick = {
                                    onAction(
                                        ChartAction.OnGotoDetail(
                                            expenses = it.items,
                                            type = it.type,
                                        )
                                    )
                                },
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
                else -> {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(horizontal = MoneyTokens.ScreenHorizontalPadding),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                    ) {
                        MoneyCard(
                            modifier = Modifier.weight(0.5f),
                            contentPadding = PaddingValues(18.dp),
                        ) {
                            ChartLayout(
                                modifier = Modifier.fillMaxWidth(),
                                state = state,
                            )
                        }
                        MoneyCard(
                            modifier = Modifier
                                .weight(0.5f)
                                .verticalScroll(rememberScrollState()),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                        ) {
                            ExpenseDetailLazyGrid(
                                modifier = Modifier.fillMaxWidth(),
                                items = state.items,
                                sumTotal = state.totalExpense,
                                onItemClick = {
                                    onAction(
                                        ChartAction.OnGotoDetail(
                                            expenses = it.items,
                                            type = it.type,
                                        )
                                    )
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
internal fun ChartScreenPreview() {
    val sampleType = Type(
        id = "1",
        typeIdTimestamp = 0L,
        name = "食",
        colorArgb = 0xFFE2D94D.toInt(),
        order = 0,
    )
    val sampleExpense = Expense(
        id = "1",
        categoryId = 1,
        typeId = 1L,
        description = "午餐",
        cost = 1500L,
        type = sampleType,
    )
    val sampleChart = core.domain.model.chart.Chart(
        type = sampleType,
        items = listOf(sampleExpense),
    )

    app.presentation.AppTheme {
        ChartScreen(
            state = ChartState(
                items = listOf(sampleChart),
                totalExpense = 1500,
                spendingLimit = 20000,
                nowDateYear = "2026",
                nowDateMonth = "11",
            ),
            isDebug = true,
        )
    }
}

@Preview
@Composable
internal fun ChartScreenEmptyPreview() {
    app.presentation.AppTheme {
        ChartScreen(
            state = ChartState(
                items = emptyList(),
                totalExpense = 0,
                nowDateYear = "2026",
                nowDateMonth = "11",
            ),
            isDebug = true,
        )
    }
}
