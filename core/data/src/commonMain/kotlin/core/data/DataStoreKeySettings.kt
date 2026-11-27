package core.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import core.domain.KeySettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreKeySettings(
    private val dataStore: DataStore<Preferences>
) : KeySettings {

    private companion object {
        val DARK_THEME_KEY = booleanPreferencesKey(KeySettings.IS_DARK_THEME)
        val BUDGET_WARN_MID_KEY = intPreferencesKey(KeySettings.BUDGET_WARN_MID)
        val BUDGET_WARN_HIGH_KEY = intPreferencesKey(KeySettings.BUDGET_WARN_HIGH)
    }

    override fun observeDarkLightMode(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[DARK_THEME_KEY] ?: false
        }
    }

    override suspend fun setDarkLightMode(isDark: Boolean) {
        dataStore.edit { preferences ->
            preferences[DARK_THEME_KEY] = isDark
        }
    }

    override fun observeBudgetWarnMidPercent(): Flow<Int> {
        return dataStore.data.map { preferences ->
            preferences[BUDGET_WARN_MID_KEY] ?: KeySettings.DEFAULT_BUDGET_WARN_MID
        }
    }

    override suspend fun setBudgetWarnMidPercent(percent: Int) {
        dataStore.edit { preferences ->
            preferences[BUDGET_WARN_MID_KEY] = percent
        }
    }

    override fun observeBudgetWarnHighPercent(): Flow<Int> {
        return dataStore.data.map { preferences ->
            preferences[BUDGET_WARN_HIGH_KEY] ?: KeySettings.DEFAULT_BUDGET_WARN_HIGH
        }
    }

    override suspend fun setBudgetWarnHighPercent(percent: Int) {
        dataStore.edit { preferences ->
            preferences[BUDGET_WARN_HIGH_KEY] = percent
        }
    }
}
