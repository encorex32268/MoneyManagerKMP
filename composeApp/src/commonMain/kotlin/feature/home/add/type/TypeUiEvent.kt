package feature.home.add.type

sealed interface TypeUiEvent {
    data object OnBack: TypeUiEvent
}