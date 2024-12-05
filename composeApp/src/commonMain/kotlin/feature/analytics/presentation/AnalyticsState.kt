package feature.analytics.presentation

import feature.analytics.presentation.model.DataPoint
import feature.analytics.presentation.model.ValueLabel
import feature.core.domain.model.Expense

data class AnalyticsState(
    val dateFilter: DateFilter = DateFilter.SEVEN_DAYS,
    val dataPoints: List<DataPoint> = emptyList(),
    val incomeDataPoints: List<DataPoint> = emptyList(),
    val selectedDataPoint: DataPoint? = null,
    val incomeSelectedDataPoint: DataPoint? = null,
    val expenseSum: Long = 0,
    val incomeSum: Long= 0
)

enum class DateFilter(val text: String) {
    SEVEN_DAYS("7D"),
    ONE_MONTHS("1M"),
    THREE_MONTHS("3M"),
    ONE_YEAR("1Y"),
    ALL("All")
}
