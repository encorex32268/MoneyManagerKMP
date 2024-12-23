package feature.analytics.presentation

import feature.analytics.presentation.model.DataPoint

data class AnalyticsState(
    val nowDateYear: String = "",
    val nowDateMonth: String = "",
    val dateFilter: DateFilter = DateFilter.SEVEN_DAYS,
    val dataPoints: List<DataPoint> = emptyList(),
    val selectedDataPoint: DataPoint? = null,
    val expenseSum: Long = 0
)

enum class DateFilter(val text: String) {
    SEVEN_DAYS("7D"),
    ONE_MONTH("1M"),
    THREE_MONTHS("3M"),
    SIX_MONTHS("6M"),
    ONE_YEAR("1Y"),
    ALL("All")
}
