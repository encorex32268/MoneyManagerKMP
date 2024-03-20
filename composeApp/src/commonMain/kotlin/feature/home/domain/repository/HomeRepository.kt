package feature.home.domain.repository

import feature.core.data.model.ExpenseEntity
import feature.core.domain.model.Expense
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    suspend fun insertExpense(expense: Expense)

    suspend fun getExpenseByStartTimeAndEndTime(
        startTimeOfMonth : Long , endTimeOfMonth : Long
    ) : Flow<List<Expense>>

    suspend fun updateExpense(expense: Expense)


}