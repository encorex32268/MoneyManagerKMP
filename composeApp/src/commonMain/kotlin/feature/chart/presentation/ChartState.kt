package feature.chart.presentation

import feature.chart.domain.model.Chart
import feature.core.domain.model.Expense

data class ChartState(
    val items: List<Chart> = emptyList(),
    val nowDateYear: String = "",
    val nowDateMonth: String = "",
    val expensesTypeList:  List<Pair<Int, List<Expense>>> = emptyList(),
    val isIncomeShow: Boolean = false

)
