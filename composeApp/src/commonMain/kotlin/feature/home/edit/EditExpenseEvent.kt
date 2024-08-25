package feature.home.edit

import feature.core.domain.model.Expense

sealed interface EditExpenseEvent {
    data class DeleteExpense(
        val expense: Expense
    ): EditExpenseEvent

    data class GetExpense(
        val expense: Expense
    ): EditExpenseEvent

    data object ShowDeleteDialog: EditExpenseEvent
    data object CloseDeleteDialog: EditExpenseEvent
}