package core.domain.mapper

import core.data.model.TypeEntity
import core.domain.model.Type

fun TypeEntity.toType(): Type {
    val hexId = id.toHexString()
    return Type(
        id = hexId,
        typeIdHex = typeIdHex.ifEmpty { hexId },
        typeIdTimestamp = typeIdTimestamp,
        name = name,
        colorArgb = colorArgb,
        order = order,
        isShow = isShow,
        categories = categories.map { it.toCategory() }
    )
}
