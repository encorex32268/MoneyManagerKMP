package feature.home.presentation

import core.domain.model.Expense
import feature.home.presentation.model.ExpenseUi

sealed interface HomeAction {
    data class OnDatePick(val year: Int?=null, val month: Int?=null): HomeAction

    data object OnGotoAddScreen: HomeAction
    data class OnGotoEditScreen(val expenseUi: ExpenseUi): HomeAction

    data class OnSpendingLimitChange(val expenseLimitText: String): HomeAction

    data object OnExpenseLimitClick: HomeAction
}