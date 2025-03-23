package feature.analytics.domain

import core.domain.model.Expense
import kotlinx.coroutines.flow.Flow

interface AnalyticsRepository {
    fun getAllExpense(): Flow<List<Expense>>
}