package feature.core.domain.mapper

import feature.core.data.model.SpendingLimitEntity
import feature.core.domain.model.SpendingLimit

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
