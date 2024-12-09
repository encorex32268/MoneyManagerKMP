package feature.chart.presentation

import feature.core.domain.model.chart.Chart


data class ChartState(
    val items: List<Chart> = emptyList(),
    val incomeItems: List<Chart> = emptyList(),
    val nowDateYear: String = "",
    val nowDateMonth: String = "",
    val isIncomeShown: Boolean = false
)
