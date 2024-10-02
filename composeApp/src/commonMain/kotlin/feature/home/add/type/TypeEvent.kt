package feature.home.add.type

import feature.core.presentation.reorderable.ItemPosition

sealed interface TypeEvent {
    data class OnNew(
        val type: TypeUi
    ): TypeEvent

    data class OnHide(
        val type: TypeUi
    ): TypeEvent

    data class OnItemMove(
        val fromItemPosition: ItemPosition,
        val toItemPosition: ItemPosition
    ): TypeEvent

    data object OnBackClick: TypeEvent
}