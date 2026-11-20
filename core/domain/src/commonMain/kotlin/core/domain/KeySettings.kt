package core.domain

interface KeySettings {

    companion object{
        const val CLOSE_AD_BANNER = "close_ad_banner"
        const val IS_DARK_THEME = "darkLightMode"
        const val IS_REALM_MIGRATED = "is_realm_migrated"
    }

    fun setCloseAdBanner(isClosed: Boolean)
    fun getCloseAdBanner(): Boolean

    fun setDarkLightMode(isDark: Boolean)
    fun getDarkLightMode(): Boolean

    fun setRealmMigrated(isMigrated: Boolean)
    fun isRealmMigrated(): Boolean
}