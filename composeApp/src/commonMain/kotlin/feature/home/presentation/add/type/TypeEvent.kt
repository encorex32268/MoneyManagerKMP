package feature.home.presentation.add.type

import feature.home.presentation.reorderable.ItemPosition

sealed interface TypeEvent {
    data class OnNew(
        val type: TypeUi
    ): TypeEvent

    data class OnHide(
        val type: TypeUi
    ): TypeEvent

    data class OnShow(
        val type: TypeUi
    ): TypeEvent

    data class OnItemMove(
        val fromItemPosition: ItemPosition,
        val toItemPosition: ItemPosition
    ): TypeEvent

    data class OnDelete(
        val type: TypeUi
    ): TypeEvent

    data class OnDragEnd(
        val startIndex: Int,
        val endIndex: Int
    ): TypeEvent

    data object OnBackClick: TypeEvent
}