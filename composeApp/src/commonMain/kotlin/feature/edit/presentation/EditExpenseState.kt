package feature.edit.presentation

import feature.core.domain.model.Expense

data class EditExpenseState(
    val currentExpense: Expense?= null
)
