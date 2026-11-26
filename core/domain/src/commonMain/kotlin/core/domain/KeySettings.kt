package core.domain

interface KeySettings {

    companion object {
        const val IS_DARK_THEME = "darkLightMode"
        const val IS_REALM_MIGRATED = "is_realm_migrated"
        const val BUDGET_WARN_MID = "budget_warn_mid"
        const val BUDGET_WARN_HIGH = "budget_warn_high"
        const val DEFAULT_BUDGET_WARN_MID = 50
        const val DEFAULT_BUDGET_WARN_HIGH = 70
        const val IS_DATABASE_UPDATE_NOTICE_SHOWN = "is_database_update_notice_shown"
    }

    fun setDarkLightMode(isDark: Boolean)
    fun getDarkLightMode(): Boolean

    fun setRealmMigrated(isMigrated: Boolean)
    fun isRealmMigrated(): Boolean

    fun setBudgetWarnMidPercent(percent: Int) {}
    fun getBudgetWarnMidPercent(): Int = DEFAULT_BUDGET_WARN_MID

    fun setBudgetWarnHighPercent(percent: Int) {}
    fun getBudgetWarnHighPercent(): Int = DEFAULT_BUDGET_WARN_HIGH

    fun setDatabaseUpdateNoticeShown(isShown: Boolean) {}
    fun isDatabaseUpdateNoticeShown(): Boolean = false
}
