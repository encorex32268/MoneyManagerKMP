package core.data

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.set
import core.domain.KeySettings

class DefaultKeySettings(
    private val settings: Settings
) : KeySettings {

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

    override fun setBudgetWarnMidPercent(percent: Int) {
        settings.set(
            key = KeySettings.BUDGET_WARN_MID,
            value = percent
        )
    }

    override fun getBudgetWarnMidPercent(): Int {
        return settings.get(
            key = KeySettings.BUDGET_WARN_MID,
            defaultValue = KeySettings.DEFAULT_BUDGET_WARN_MID
        )
    }

    override fun setBudgetWarnHighPercent(percent: Int) {
        settings.set(
            key = KeySettings.BUDGET_WARN_HIGH,
            value = percent
        )
    }

    override fun getBudgetWarnHighPercent(): Int {
        return settings.get(
            key = KeySettings.BUDGET_WARN_HIGH,
            defaultValue = KeySettings.DEFAULT_BUDGET_WARN_HIGH
        )
    }
}
