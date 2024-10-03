package feature.home.presentation.add.type

sealed interface TypeUiEvent {
    data object OnBack: TypeUiEvent
}