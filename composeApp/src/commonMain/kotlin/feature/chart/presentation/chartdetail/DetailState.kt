package feature.chart.presentation.chartdetail

import core.domain.model.Expense
import core.domain.model.Type

data class DetailState(
    val total: Long = 0L,
    val type: Type,
    val items: List<Pair<String, List<Expense>>> = emptyList(),
)
