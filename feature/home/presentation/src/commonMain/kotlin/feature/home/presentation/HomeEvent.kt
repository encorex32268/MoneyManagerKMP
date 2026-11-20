package feature.home.presentation

import core.domain.model.Expense
import feature.home.presentation.model.ExpenseUi

sealed interface HomeEvent {
    data class OnDatePick(val year: Int?=null, val month: Int?=null): HomeEvent

    data object OnGotoAddScreen: HomeEvent
    data class OnGotoEditScreen(val expenseUi: ExpenseUi): HomeEvent

    data class OnSpendingLimitChange(val expenseLimitText: String): HomeEvent

    data object OnExpenseLimitClick: HomeEvent
}