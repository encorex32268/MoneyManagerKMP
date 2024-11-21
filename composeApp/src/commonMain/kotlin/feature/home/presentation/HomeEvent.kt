package feature.home.presentation

import feature.core.domain.model.Expense

sealed interface HomeEvent {
    data class OnDatePick(
        val isInit: Boolean = false,
        val year: Int?=null,
        val month: Int?=null
    ): HomeEvent

    data object OnGotoAddScreen: HomeEvent
    data object OnGotoChartScreen: HomeEvent
    data class OnGotoEditScreen(
        val expense: Expense
    ): HomeEvent
}