package feature.chart.presentation

import core.domain.model.Expense
import core.domain.model.Type

sealed interface ChartEvent {
    data class OnDatePick(
        val isInit: Boolean = false,
        val year: Int?=null,
        val month: Int?=null
    ): ChartEvent

    data class OnGotoDetail(
        val expenses: List<Expense>,
        val type: Type
    ): ChartEvent
}