package feature.home.presentation.add

sealed interface AddUiEvent {
    data object Success: AddUiEvent
    data object Fail: AddUiEvent
}