package feature.home.presentation.add.type.category

import core.presentation.model.CategoryUi
import feature.home.presentation.reorderable.ItemPosition


sealed interface TypeCategoryEditEvent {

    data object OnBackClick: TypeCategoryEditEvent

    data object OnSaveClick: TypeCategoryEditEvent
    data class OnTypeEdit(
        val name: String,
        val colorArgb: Int
    ): TypeCategoryEditEvent

    data class OnItemMove(
        val fromItemPosition: ItemPosition,
        val toItemPosition: ItemPosition
    ): TypeCategoryEditEvent

    data class OnItemRemove(
        val categoryUi: CategoryUi
    ): TypeCategoryEditEvent

    data class OnItemAdd(
        val categoryUi: CategoryUi
    ): TypeCategoryEditEvent
}