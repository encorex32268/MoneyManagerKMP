package feature.home.edit

import feature.core.domain.model.Expense

data class EditExpenseState(
    val currentExpense: Expense?= null,
    val isShowDeleteDialog: Boolean = false
)