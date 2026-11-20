package feature.analytics.domain

import core.domain.model.Expense
import core.domain.model.SpendingLimit
import core.domain.model.Type

data class BackupData(
    val expenses: List<Expense> = emptyList(),
    val types: List<Type> = emptyList(),
    val spendingLimits: List<SpendingLimit> = emptyList(),
)
