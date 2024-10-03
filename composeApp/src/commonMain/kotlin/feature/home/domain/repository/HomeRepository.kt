package feature.home.domain.repository

import feature.core.domain.model.Expense
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    suspend fun getExpenseByTime(
        startTimeOfMonth: Long,
        endTimeOfMonth: Long
    ): Flow<List<Expense>>
}