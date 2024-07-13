package feature.core.domain.model.chart

import feature.core.domain.model.Expense

data class Chart(
    val typeId: Int,
    val expenseItems: List<Expense> = emptyList()
)
