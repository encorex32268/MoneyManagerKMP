package core.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "spending_limits")
data class SpendingLimitRoomEntity(
    @PrimaryKey
    val id: String,
    val year: Int,
    val month: Int,
    val limit: Long
)
