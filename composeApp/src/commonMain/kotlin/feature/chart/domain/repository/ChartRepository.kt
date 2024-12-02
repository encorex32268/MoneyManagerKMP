package feature.chart.domain.repository

import feature.core.domain.model.chart.Chart
import kotlinx.coroutines.flow.Flow

interface ChartRepository {
    fun getExpenseByTime(
        startTimeOfMonth: Long,
        endTimeOfMonth: Long
    ): Flow<List<Chart>>
}