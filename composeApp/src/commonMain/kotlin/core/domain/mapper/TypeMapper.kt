package core.domain.mapper

import core.data.model.TypeEntity
import core.domain.model.Type
import feature.home.presentation.add.type.TypeUi
import io.realm.kotlin.ext.toRealmList

fun TypeEntity.toType(): Type {
    return Type(
        id = id,
        typeIdHex = typeIdHex,
        typeIdTimestamp = typeIdTimestamp,
        name = name,
        colorArgb =  colorArgb,
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
        colorArgb =  colorArgb,
        order = order,
        isShow = isShow,
        categories = categories.map { it.toCategoryEntity() }.toRealmList()
    )
}

fun Type.toTypeUi(): TypeUi {
    return TypeUi(
        id = id,
        typeIdHex = typeIdHex,
        typeIdTimestamp = typeIdTimestamp,
        name = name,
        colorArgb = colorArgb,
        order =  order,
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
        order =  order,
        isShow = isShow,
        categories = categories.map { it.toCategory() }
    )
}