package feature.chart.presentation

import feature.home.presentation.HomeEvent

sealed interface ChartEvent {
    data class OnDatePick(
        val isInit: Boolean = false,
        val year: Int?=null,
        val month: Int?=null
    ): ChartEvent

    data object OnChartClick: ChartEvent
}