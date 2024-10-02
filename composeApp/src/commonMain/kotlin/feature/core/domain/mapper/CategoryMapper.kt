package feature.core.domain.mapper

import androidx.compose.ui.graphics.toArgb
import feature.core.data.model.CategoryEntity
import feature.core.domain.model.Category
import feature.core.presentation.CategoryList
import feature.core.presentation.model.CategoryUi


fun CategoryEntity.toCategory(): Category{
    return Category(
        id = id,
        name = name,
        order = order,
        typeId = typeId,
    )
}

fun Category.toCategoryEntity(): CategoryEntity{
    return CategoryEntity(
        id = id,
        name = name,
        order = order?:0,
        typeId = typeId?:0,
    )
}

fun CategoryUi.toCategory(): Category {
    return Category(
        id = id,
        name = name,
        order = order,
        typeId = typeId,
    )
}

fun Category.toCategoryUi(): CategoryUi {
    return CategoryUi(
        id = id,
        isClick = false,
        name = name,
        order = order?:0,
        typeId = typeId,
        colorArgb = if (typeId != null) CategoryList.getColorByTypeId(typeId!!).toArgb() else null,
    )
}