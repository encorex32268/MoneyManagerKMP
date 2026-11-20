package core.domain.mapper

import core.domain.model.Type
import feature.home.presentation.add.type.TypeUi

fun Type.toTypeUi(): TypeUi {
    return TypeUi(
        id = id,
        typeIdHex = typeIdHex,
        typeIdTimestamp = typeIdTimestamp,
        name = name,
        colorArgb = colorArgb,
        order = order,
        isShow = isShow,
        categories = categories.map { it.toCategoryUi() }
    )
}

fun TypeUi.toType(): Type {
    return Type(
        id = id,
        typeIdHex = typeIdHex,
        typeIdTimestamp = typeIdTimestamp,
        name = name,
        colorArgb = colorArgb,
        order = order,
        isShow = isShow,
        categories = categories.map { it.toCategory() }
    )
}
