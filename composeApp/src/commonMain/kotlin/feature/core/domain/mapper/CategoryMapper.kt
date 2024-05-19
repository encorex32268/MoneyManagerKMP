package feature.core.domain.mapper

import feature.core.domain.model.Category
import feature.core.presentation.model.CategoryUi

fun CategoryUi.toCategory(): Category {
    return Category(
        id = id,
        categoryId = categoryId,
        typeId = typeId
    )
}

fun Category.toCategoryUi(): CategoryUi {
    return CategoryUi(
        id = id,
        categoryId = categoryId,
        typeId = typeId,
        isClick = false
    )
}