package feature.home.presentation.edit


sealed interface EditExpenseEvent {
    data object OnBackClick: EditExpenseEvent
    data object OnDelete: EditExpenseEvent
    data object OnGoAddScreenClick: EditExpenseEvent
    data class OnContentChange(
        val text: String
    ): EditExpenseEvent
    data object OnSaveClick: EditExpenseEvent
}