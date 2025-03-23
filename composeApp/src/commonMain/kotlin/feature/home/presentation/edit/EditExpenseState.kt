package feature.home.presentation.edit

import feature.core.domain.model.Expense
import feature.core.domain.model.Type

data class EditExpenseState(
    val currentExpense: Expense?= null,
    val typeItems: List<Type> = emptyList(),
    val isShowSaveIcon: Boolean = false
)