package core.domain.mapper

import core.data.model.TypeEntity
import core.domain.model.Type
import io.realm.kotlin.ext.toRealmList

fun TypeEntity.toType(): Type {
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

fun Type.toTypeEntity(): TypeEntity {
    return TypeEntity(
        id = id,
        typeIdHex = typeIdHex,
        typeIdTimestamp = typeIdTimestamp,
        name = name,
        colorArgb = colorArgb,
        order = order,
        isShow = isShow,
        categories = categories.map { it.toCategoryEntity() }.toRealmList()
    )
}
