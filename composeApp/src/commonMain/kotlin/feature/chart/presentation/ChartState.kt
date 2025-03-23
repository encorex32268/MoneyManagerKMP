package feature.chart.presentation

import core.domain.model.chart.Chart


data class ChartState(
    val items: List<core.domain.model.chart.Chart> = emptyList(),
    val totalExpense: Long = 0L,
    val spendingLimit: Long = 0L,
    val nowDateYear: String = "",
    val nowDateMonth: String = "",
)
