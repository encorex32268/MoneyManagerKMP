package feature.core.domain.repository

import feature.core.domain.model.Expense
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {

    fun getExpenseByTime(
        startTimeOfMonth: Long,
        endTimeOfMonth: Long
    ): Flow<List<Expense>>

    fun getRecentlyExpenses() : Flow<List<Expense>>

    fun getExpenseByTypeId(
        typeId: Long
    ): Flow<List<Expense>>

    suspend fun delete(expense: Expense)
    suspend fun update(expense: Expense)
    suspend fun insert(expense: Expense)

}