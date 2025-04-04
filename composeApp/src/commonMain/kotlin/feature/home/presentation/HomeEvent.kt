package feature.home.presentation

import core.domain.model.Expense

sealed interface HomeEvent {
    data class OnDatePick(
        val isInit: Boolean = false,
        val year: Int?=null,
        val month: Int?=null
    ): HomeEvent

    data object OnGotoAddScreen: HomeEvent
    data class OnGotoEditScreen(
        val expense: Expense
    ): HomeEvent

    data class OnSpendingLimitChange(
        val expenseLimitText: String
    ): HomeEvent

    data object OnExpenseLimitClick: HomeEvent
}