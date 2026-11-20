package core.data.database

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

expect class DatabaseFactory {
    fun create(): RoomDatabase.Builder<MoneyManagerDatabase>
}

fun buildMoneyManagerDatabase(factory: DatabaseFactory): MoneyManagerDatabase {
    return factory.create()
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}
