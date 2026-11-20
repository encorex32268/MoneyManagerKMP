@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package core.data.database

import androidx.room.Room
import androidx.room.RoomDatabase
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual class DatabaseFactory {
    actual fun create(): RoomDatabase.Builder<MoneyManagerDatabase> {
        val dbFilePath = documentDirectory() + "/${MoneyManagerDatabase.DB_NAME}"
        return Room.databaseBuilder<MoneyManagerDatabase>(
            name = dbFilePath
        )
    }

    private fun documentDirectory(): String {
        val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null
        )
        return requireNotNull(documentDirectory?.path)
    }
}
