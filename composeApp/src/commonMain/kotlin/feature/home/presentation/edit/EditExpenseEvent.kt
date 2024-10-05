package feature.home.presentation.edit

import feature.core.domain.model.Expense

sealed interface EditExpenseEvent {
    data object OnDelete: EditExpenseEvent
}