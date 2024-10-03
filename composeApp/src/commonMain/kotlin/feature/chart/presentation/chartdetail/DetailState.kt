package feature.chart.presentation.chartdetail

import feature.core.domain.model.Expense

data class DetailState(
    val total: Long = 0L,
    val typeId: Int = 0,
    val items: List<Pair<String, List<Expense>>> = emptyList(),
)
