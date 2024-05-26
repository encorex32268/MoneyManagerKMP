package feature.chart.presentation

import feature.chart.domain.model.Chart
import feature.core.domain.model.Expense


data class ChartState(
    val expenseTypeList: List<Chart>  = emptyList(),
    val incomeTypeList: List<Chart>  = emptyList(),
    val nowDateYear: String = "",
    val nowDateMonth: String = "",
    val isIncomeShown: Boolean = false
)
