package feature.chart.presentation

import feature.core.domain.model.Expense
import feature.core.domain.model.Type

sealed interface ChartEvent {
    data class OnDatePick(
        val isInit: Boolean = false,
        val year: Int?=null,
        val month: Int?=null
    ): ChartEvent

    data class OnTypeChange(
        val isIncome: Boolean
    ): ChartEvent

    data class OnGotoDetail(
        val expenses: List<Expense>,
        val type: Type
    ): ChartEvent
}