package feature.add.presentation

sealed interface AddUiEvent {
    data object Success: AddUiEvent
    data object Fail: AddUiEvent
}