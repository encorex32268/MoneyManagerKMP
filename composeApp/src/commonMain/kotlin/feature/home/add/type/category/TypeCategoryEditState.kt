package feature.home.add.type.category

import feature.core.domain.mapper.toCategoryUi
import feature.core.presentation.CategoryList
import feature.core.presentation.model.CategoryUi
import feature.home.add.type.TypeUi

data class TypeCategoryEditState(
    val categories: List<CategoryUi> = CategoryList.items.map {
        it.toCategoryUi()
    },
    val typeUi: TypeUi
)
