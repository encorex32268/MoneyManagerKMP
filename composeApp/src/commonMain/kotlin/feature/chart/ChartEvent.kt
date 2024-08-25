package feature.chart

sealed interface ChartEvent {
    data class OnDatePick(
        val isInit: Boolean = false,
        val year: Int?=null,
        val month: Int?=null
    ): ChartEvent

    data class OnTypeChange(
        val isIncome: Boolean
    ): ChartEvent
}