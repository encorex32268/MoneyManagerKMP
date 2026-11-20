package core.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import core.data.database.entity.ExpenseRoomEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Query("SELECT * FROM expenses WHERE timestamp <= :endTimeOfMonth AND timestamp >= :startTimeOfMonth")
    fun getExpenseByTime(
        startTimeOfMonth: Long,
        endTimeOfMonth: Long
    ): Flow<List<ExpenseRoomEntity>>

    @Query("SELECT * FROM expenses WHERE categoryId != 0 ORDER BY timestamp DESC")
    fun getNonZeroCategoryExpensesDesc(): Flow<List<ExpenseRoomEntity>>

    @Query("SELECT * FROM expenses WHERE typeId = :typeId")
    fun getExpenseByTypeId(typeId: Long): Flow<List<ExpenseRoomEntity>>

    @Query("SELECT * FROM expenses WHERE id = :id LIMIT 1")
    fun getExpenseById(id: String): Flow<ExpenseRoomEntity?>

    @Query("SELECT * FROM expenses")
    fun getAllFlow(): Flow<List<ExpenseRoomEntity>>

    @Query("SELECT * FROM expenses")
    suspend fun getAllOnce(): List<ExpenseRoomEntity>

    @Upsert
    suspend fun upsert(expense: ExpenseRoomEntity)

    @Upsert
    suspend fun upsertAll(expenses: List<ExpenseRoomEntity>)

    @Query("DELETE FROM expenses WHERE id = :id")
    suspend fun deleteById(id: String)
}
