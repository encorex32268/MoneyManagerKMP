package core.domain.mapper

import core.data.model.CategoryEntity
import core.domain.model.Category

fun CategoryEntity.toCategory(): Category {
    return Category(
        id = id,
        name = name,
        order = order,
        typeId = typeId,
    )
}

fun Category.toCategoryEntity(): CategoryEntity {
    return CategoryEntity(
        id = id,
        name = name,
        order = order ?: 0,
        typeId = typeId ?: 0,
    )
}
