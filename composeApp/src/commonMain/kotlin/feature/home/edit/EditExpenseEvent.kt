package feature.home.edit

import feature.core.domain.model.Expense

sealed interface EditExpenseEvent {
    data object OnDelete: EditExpenseEvent

    data class GetExpense(
        val expense: Expense
    ): EditExpenseEvent


}