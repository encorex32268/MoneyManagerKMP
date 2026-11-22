package core.domain

interface KeySettings {

    companion object{
        const val IS_DARK_THEME = "darkLightMode"
        const val IS_REALM_MIGRATED = "is_realm_migrated"
    }

    fun setDarkLightMode(isDark: Boolean)
    fun getDarkLightMode(): Boolean

    fun setRealmMigrated(isMigrated: Boolean)
    fun isRealmMigrated(): Boolean
}