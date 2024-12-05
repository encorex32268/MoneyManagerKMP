package feature.analytics.presentation

import feature.analytics.presentation.model.DataPoint

sealed interface AnalyticsEvent {

    data class OnDateFilterChange(
        val dateFilter: DateFilter
    ): AnalyticsEvent

    data object OnBackupClick: AnalyticsEvent

    data class OnSelectDataPoint(
        val dataPoint: DataPoint
    ): AnalyticsEvent

    data class OnIncomeSelectDataPoint(
        val dataPoint: DataPoint
    ): AnalyticsEvent
}