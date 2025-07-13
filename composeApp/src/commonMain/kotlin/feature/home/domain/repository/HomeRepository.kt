package feature.home.domain.repository

import core.domain.model.Expense
import feature.home.presentation.model.ExpenseUi
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getExpenseByTime(
        startTimeOfMonth: Long,
        endTimeOfMonth: Long
    ): Flow<List<Expense>>
}