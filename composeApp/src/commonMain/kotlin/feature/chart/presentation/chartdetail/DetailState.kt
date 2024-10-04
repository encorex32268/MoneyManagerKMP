package feature.chart.presentation.chartdetail

import feature.core.domain.model.Expense
import feature.core.domain.model.Type

data class DetailState(
    val total: Long = 0L,
    val type: Type,
    val items: List<Pair<String, List<Expense>>> = emptyList(),
)
