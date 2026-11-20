package core.data.database.mapper

import core.data.database.entity.CategoryRoomEntity
import core.data.database.entity.ExpenseRoomEntity
import core.data.database.entity.SpendingLimitRoomEntity
import core.data.database.entity.TypeRoomEntity
import core.data.database.entity.TypeWithCategories
import core.domain.model.Category
import core.domain.model.Expense
import core.domain.model.SpendingLimit
import core.domain.model.Type

fun ExpenseRoomEntity.toExpense(): Expense {
    return Expense(
        id = id,
        idString = id,
        categoryId = categoryId,
        typeId = typeId,
        description = description,
        isIncome = isIncome,
        cost = cost,
        timestamp = timestamp,
        content = content
    )
}

fun Expense.toRoomEntity(): ExpenseRoomEntity {
    return ExpenseRoomEntity(
        id = id,
        categoryId = categoryId,
        typeId = typeId,
        description = description,
        isIncome = isIncome,
        cost = cost,
        timestamp = timestamp,
        content = content
    )
}

fun CategoryRoomEntity.toCategory(): Category {
    return Category(
        id = id,
        name = name,
        order = order,
        typeId = typeId
    )
}

fun Category.toRoomEntity(parentTypeId: String): CategoryRoomEntity {
    return CategoryRoomEntity(
        id = id,
        name = name,
        order = order ?: 0,
        typeId = typeId ?: 0L,
        parentTypeId = parentTypeId
    )
}

fun TypeWithCategories.toType(): Type {
    return Type(
        id = type.id,
        typeIdHex = type.typeIdHex,
        typeIdTimestamp = type.typeIdTimestamp,
        name = type.name,
        colorArgb = type.colorArgb,
        order = type.order,
        isShow = type.isShow,
        categories = categories.sortedBy { it.order }.map { it.toCategory() }
    )
}

fun Type.toRoomTypeAndCategories(): Pair<TypeRoomEntity, List<CategoryRoomEntity>> {
    val typeEntity = TypeRoomEntity(
        id = id,
        typeIdHex = typeIdHex,
        typeIdTimestamp = typeIdTimestamp,
        name = name,
        colorArgb = colorArgb,
        order = order,
        isShow = isShow
    )
    val categoryEntities = categories.map { it.toRoomEntity(parentTypeId = id) }
    return typeEntity to categoryEntities
}

fun SpendingLimitRoomEntity.toSpendingLimit(): SpendingLimit {
    return SpendingLimit(
        id = id,
        year = year,
        month = month,
        limit = limit
    )
}

fun SpendingLimit.toRoomEntity(): SpendingLimitRoomEntity {
    return SpendingLimitRoomEntity(
        id = id,
        year = year,
        month = month,
        limit = limit
    )
}
