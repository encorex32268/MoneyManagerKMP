package feature.analytics.presentation

import feature.analytics.presentation.model.DataPoint

sealed interface AnalyticsAction {

    data class OnDateFilterChange(
        val dateFilter: DateFilter
    ): AnalyticsAction

    data object OnBackupClick: AnalyticsAction

    data class OnSelectDataPoint(
        val dataPoint: DataPoint
    ): AnalyticsAction

    data class OnDatePick(
        val year: Int?=null,
        val month: Int?=null
    ): AnalyticsAction

    data object OnDarkLightModeSwitch: AnalyticsAction
}