package core.data

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set
import core.domain.KeySettings

class DefaultKeySettings(
    private val settings: Settings
): KeySettings {

    override fun setDarkLightMode(isDark: Boolean) {
        settings.set(
            key = KeySettings.IS_DARK_THEME,
            value = isDark
        )
    }

    override fun getDarkLightMode(): Boolean {
        return settings.get(
            key = KeySettings.IS_DARK_THEME,
            defaultValue = false
        )
    }

    override fun setRealmMigrated(isMigrated: Boolean) {
        settings.set(
            key = KeySettings.IS_REALM_MIGRATED,
            value = isMigrated
        )
    }

    override fun isRealmMigrated(): Boolean {
        return settings.get(
            key = KeySettings.IS_REALM_MIGRATED,
            defaultValue = false
        )
    }
}