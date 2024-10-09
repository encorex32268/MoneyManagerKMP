package feature.home.presentation.edit

sealed interface EditExpenseUiEvent {
    data object OnBack: EditExpenseUiEvent
}