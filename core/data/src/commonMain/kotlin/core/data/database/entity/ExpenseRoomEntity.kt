package core.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class ExpenseRoomEntity(
    @PrimaryKey
    val id: String,
    val categoryId: Int,
    val typeId: Long,
    val description: String,
    val isIncome: Boolean,
    val cost: Long,
    val timestamp: Long,
    val content: String
)
