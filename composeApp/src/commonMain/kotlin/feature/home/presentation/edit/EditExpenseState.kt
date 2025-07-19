package feature.home.presentation.edit

import core.domain.model.Expense
import core.domain.model.Type
import feature.home.presentation.model.ExpenseUi

data class EditExpenseState(
    val currentExpenseUi: ExpenseUi?= null,
    val isShowSaveIcon: Boolean = false
)