package feature.chart.domain.repository

import feature.core.domain.model.Expense
import kotlinx.coroutines.flow.Flow

interface ChartRepository {
    suspend fun getExpenseByTime(
        startTimeOfMonth: Long,
        endTimeOfMonth: Long
    ): Flow<List<Expense>>
}