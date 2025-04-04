package core.domain.mapper

import androidx.compose.ui.graphics.toArgb
import core.data.model.CategoryEntity
import core.domain.model.Category
import core.presentation.CategoryList
import core.presentation.model.CategoryUi


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