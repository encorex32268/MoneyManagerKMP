package core.domain.mapper

import core.data.model.SpendingLimitEntity
import core.domain.model.SpendingLimit

fun SpendingLimitEntity.toSpendingLimit(): SpendingLimit {
    return SpendingLimit(
        id = id.toHexString(),
        year = year,
        month = month,
        limit = limit
    )
}
