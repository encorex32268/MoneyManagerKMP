package feature.core.domain.model.chart

import feature.core.domain.model.Expense
import feature.core.domain.model.Type

data class Chart(
    val type: Type,
    val items: List<Expense> = emptyList()
)
