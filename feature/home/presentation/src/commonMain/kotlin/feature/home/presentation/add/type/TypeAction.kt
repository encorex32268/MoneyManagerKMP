package feature.home.presentation.add.type

sealed interface TypeAction {
    data class OnNew(
        val type: TypeUi
    ) : TypeAction

    data class OnHide(
        val type: TypeUi
    ) : TypeAction

    data class OnShow(
        val type: TypeUi
    ) : TypeAction

    data class OnMoveUp(
        val type: TypeUi
    ) : TypeAction

    data class OnDelete(
        val type: TypeUi
    ) : TypeAction

    data object OnBackClick : TypeAction
}
