package feature.home.presentation.add.type.category

import core.domain.mapper.toCategoryUi
import core.presentation.CategoryList
import core.presentation.model.CategoryUi
import feature.home.presentation.add.type.TypeUi

data class TypeCategoryEditState(
    val categories: List<CategoryUi> = CategoryList.items.map {
        it.toCategoryUi()
    },
    val typeUi: TypeUi
)
