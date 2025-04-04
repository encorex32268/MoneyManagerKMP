package core.domain.model.chart

import core.domain.model.Expense
import core.domain.model.Type

data class Chart(
    val type: Type,
    val items: List<Expense> = emptyList()
)
