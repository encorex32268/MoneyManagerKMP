package core.domain.repository

import core.domain.model.Expense
import core.domain.model.SpendingLimit
import kotlinx.coroutines.flow.Flow

interface SpendingLimitRepository {
    fun getAll(): Flow<List<SpendingLimit>>
    fun getSpendingLimit(year: Int , month: Int): Flow<SpendingLimit?>
    suspend fun insert(spendingLimit: SpendingLimit)
    suspend fun update(spendingLimit: SpendingLimit)
    suspend fun delete(spendingLimit: SpendingLimit)

    suspend fun restore(spendingLimits: List<SpendingLimit>)
}