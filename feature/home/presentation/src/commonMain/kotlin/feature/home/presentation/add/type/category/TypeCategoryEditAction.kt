package feature.home.presentation.add.type.category

import core.presentation.model.CategoryUi
import feature.home.presentation.reorderable.ItemPosition


sealed interface TypeCategoryEditAction {

    data object OnBackClick: TypeCategoryEditAction

    data object OnSaveClick: TypeCategoryEditAction
    data class OnTypeEdit(
        val name: String,
        val colorArgb: Int
    ): TypeCategoryEditAction

    data class OnItemMove(
        val fromItemPosition: ItemPosition,
        val toItemPosition: ItemPosition
    ): TypeCategoryEditAction

    data class OnItemRemove(
        val categoryUi: CategoryUi
    ): TypeCategoryEditAction

    data class OnItemAdd(
        val categoryUi: CategoryUi
    ): TypeCategoryEditAction
}