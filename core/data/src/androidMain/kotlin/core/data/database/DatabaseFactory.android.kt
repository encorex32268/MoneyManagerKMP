package core.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

actual class DatabaseFactory(
    private val context: Context
) {
    actual fun create(): RoomDatabase.Builder<MoneyManagerDatabase> {
        val appContext = context.applicationContext
        val dbFile = appContext.getDatabasePath(MoneyManagerDatabase.DB_NAME)
        return Room.databaseBuilder<MoneyManagerDatabase>(
            context = appContext,
            name = dbFile.absolutePath
        )
    }
}
