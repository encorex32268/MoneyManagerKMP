package feature.presentation.chart

import feature.core.domain.model.chart.Chart


data class ChartState(
    val expenseTypeList: List<Chart>  = emptyList(),
    val incomeTypeList: List<Chart>  = emptyList(),
    val nowDateYear: String = "",
    val nowDateMonth: String = "",
    val isIncomeShown: Boolean = false
)
