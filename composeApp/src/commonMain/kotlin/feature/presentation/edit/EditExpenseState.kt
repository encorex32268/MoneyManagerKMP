package feature.presentation.edit

import feature.core.domain.model.Expense

data class EditExpenseState(
    val currentExpense: Expense?= null
)