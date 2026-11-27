package core.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import core.domain.KeySettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

private class FakePreferencesDataStore : DataStore<Preferences> {
    private val _data = MutableStateFlow<Preferences>(emptyPreferences())
    override val data: Flow<Preferences> = _data

    override suspend fun updateData(transform: suspend (t: Preferences) -> Preferences): Preferences {
        val updated = transform(_data.value)
        _data.value = updated
        return updated
    }
}

class DataStoreKeySettingsTest {

    @Test
    fun `emits default values when no preferences are stored`() = runTest {
        val settings = DataStoreKeySettings(FakePreferencesDataStore())

        assertFalse(settings.observeDarkLightMode().first())
        assertEquals(KeySettings.DEFAULT_BUDGET_WARN_MID, settings.observeBudgetWarnMidPercent().first())
        assertEquals(KeySettings.DEFAULT_BUDGET_WARN_HIGH, settings.observeBudgetWarnHighPercent().first())
    }

    @Test
    fun `persists and emits updated dark mode preference`() = runTest {
        val settings = DataStoreKeySettings(FakePreferencesDataStore())

        settings.setDarkLightMode(true)
        assertTrue(settings.observeDarkLightMode().first())

        settings.setDarkLightMode(false)
        assertFalse(settings.observeDarkLightMode().first())
    }

    @Test
    fun `persists and emits updated budget warning thresholds`() = runTest {
        val settings = DataStoreKeySettings(FakePreferencesDataStore())

        settings.setBudgetWarnMidPercent(40)
        settings.setBudgetWarnHighPercent(80)

        assertEquals(40, settings.observeBudgetWarnMidPercent().first())
        assertEquals(80, settings.observeBudgetWarnHighPercent().first())
    }
}
