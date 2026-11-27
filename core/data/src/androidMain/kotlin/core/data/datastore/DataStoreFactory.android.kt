package core.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

actual class DataStoreFactory(
    private val context: Context
) {
    actual fun create(): DataStore<Preferences> {
        val appContext = context.applicationContext
        return createPreferencesDataStore(
            producePath = {
                appContext.filesDir.resolve(DATA_STORE_FILE_NAME).absolutePath
            }
        )
    }
}
