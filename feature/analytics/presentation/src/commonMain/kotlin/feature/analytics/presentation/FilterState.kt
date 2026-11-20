package feature.analytics.presentation

import feature.analytics.presentation.model.DataPoint

data class FilterState(
    val dateFilter: DateFilter = DateFilter.SEVEN_DAYS,
    val selectedYear: Int? = null,
    val selectedMonth: Int? = null,
    val selectedDataPoint: DataPoint? = null
)
