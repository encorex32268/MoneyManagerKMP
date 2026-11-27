package core.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface KeySettings {

    companion object {
        const val IS_DARK_THEME = "darkLightMode"
        const val BUDGET_WARN_MID = "budget_warn_mid"
        const val BUDGET_WARN_HIGH = "budget_warn_high"
        const val DEFAULT_BUDGET_WARN_MID = 50
        const val DEFAULT_BUDGET_WARN_HIGH = 70
    }

    fun observeDarkLightMode(): Flow<Boolean>
    suspend fun setDarkLightMode(isDark: Boolean)

    fun observeBudgetWarnMidPercent(): Flow<Int> = flowOf(DEFAULT_BUDGET_WARN_MID)
    suspend fun setBudgetWarnMidPercent(percent: Int) {}

    fun observeBudgetWarnHighPercent(): Flow<Int> = flowOf(DEFAULT_BUDGET_WARN_HIGH)
    suspend fun setBudgetWarnHighPercent(percent: Int) {}
}
