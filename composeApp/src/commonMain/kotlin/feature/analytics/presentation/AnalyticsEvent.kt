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

    data class OnDatePick(
        val year: Int?=null,
        val month: Int?=null
    ): AnalyticsEvent

    data object OnDarkLightModeSwitch: AnalyticsEvent
}