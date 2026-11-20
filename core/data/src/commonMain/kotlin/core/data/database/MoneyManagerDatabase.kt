package core.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import core.data.database.dao.ExpenseDao
import core.data.database.dao.SpendingLimitDao
import core.data.database.dao.TypeDao
import core.data.database.entity.CategoryRoomEntity
import core.data.database.entity.ExpenseRoomEntity
import core.data.database.entity.SpendingLimitRoomEntity
import core.data.database.entity.TypeRoomEntity

@Database(
    entities = [
        ExpenseRoomEntity::class,
        TypeRoomEntity::class,
        CategoryRoomEntity::class,
        SpendingLimitRoomEntity::class
    ],
    version = 1
)
@ConstructedBy(MoneyManagerDatabaseConstructor::class)
abstract class MoneyManagerDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao
    abstract fun typeDao(): TypeDao
    abstract fun spendingLimitDao(): SpendingLimitDao

    companion object {
        const val DB_NAME = "money_manager.db"
    }
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object MoneyManagerDatabaseConstructor : RoomDatabaseConstructor<MoneyManagerDatabase> {
    override fun initialize(): MoneyManagerDatabase
}
