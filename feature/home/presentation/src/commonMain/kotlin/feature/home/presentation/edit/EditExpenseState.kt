package feature.home.presentation.edit


import feature.home.presentation.model.ExpenseUi

data class EditExpenseState(
    val currentExpenseUi: ExpenseUi?= null,
    val isShowSaveIcon: Boolean = false
)