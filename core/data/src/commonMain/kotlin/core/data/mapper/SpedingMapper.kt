package core.domain.mapper

import core.data.model.SpendingLimitEntity
import core.domain.model.SpendingLimit

fun SpendingLimitEntity.toSpendingLimit(): SpendingLimit {
    return SpendingLimit(
        id = id,
        year = year,
        month = month,
        limit = limit
    )
}

fun SpendingLimit.toSpendLimitEntity(): SpendingLimitEntity {
    return SpendingLimitEntity(
        id = id,
        year = year,
        month = month,
        limit = limit
    )
}
