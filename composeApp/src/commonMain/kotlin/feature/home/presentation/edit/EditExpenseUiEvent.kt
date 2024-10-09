package feature.home.presentation.edit

import feature.core.domain.model.Expense

sealed interface EditExpenseUiEvent {
    data object OnBack: EditExpenseUiEvent
    data class OnGoAddScreen(
        val expense: Expense
    ): EditExpenseUiEvent
}