package core.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import core.data.database.entity.SpendingLimitRoomEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SpendingLimitDao {

    @Query("SELECT * FROM spending_limits")
    fun getAll(): Flow<List<SpendingLimitRoomEntity>>

    @Query("SELECT * FROM spending_limits WHERE year = :year AND month = :month LIMIT 1")
    fun getSpendingLimit(year: Int, month: Int): Flow<SpendingLimitRoomEntity?>

    @Upsert
    suspend fun upsert(spendingLimit: SpendingLimitRoomEntity)

    @Upsert
    suspend fun upsertAll(spendingLimits: List<SpendingLimitRoomEntity>)

    @Query("DELETE FROM spending_limits WHERE id = :id")
    suspend fun deleteById(id: String)
}
