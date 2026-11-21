package feature.home.presentation.add.type

import feature.home.presentation.reorderable.ItemPosition

sealed interface TypeAction {
    data class OnNew(
        val type: TypeUi
    ): TypeAction

    data class OnHide(
        val type: TypeUi
    ): TypeAction

    data class OnShow(
        val type: TypeUi
    ): TypeAction

    data class OnItemMove(
        val fromItemPosition: ItemPosition,
        val toItemPosition: ItemPosition
    ): TypeAction

    data class OnDelete(
        val type: TypeUi
    ): TypeAction

    data class OnDragEnd(
        val startIndex: Int,
        val endIndex: Int
    ): TypeAction

    data object OnBackClick: TypeAction
}