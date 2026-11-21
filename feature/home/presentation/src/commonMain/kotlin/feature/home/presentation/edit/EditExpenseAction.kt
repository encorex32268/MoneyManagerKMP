package feature.home.presentation.edit


sealed interface EditExpenseAction {
    data object OnBackClick: EditExpenseAction
    data object OnDelete: EditExpenseAction
    data object OnGoAddScreenClick: EditExpenseAction
    data class OnContentChange(
        val text: String
    ): EditExpenseAction
    data object OnSaveClick: EditExpenseAction
}