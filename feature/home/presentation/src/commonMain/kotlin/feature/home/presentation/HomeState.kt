package feature.home.presentation

import androidx.compose.ui.graphics.Color
import core.ui.MoneyTokens
import core.ui.limitsColor_0_50_Color
import core.ui.limitsColor_100_Color
import core.ui.limitsColor_50_70_Color
import feature.home.presentation.model.ExpenseUi

data class HomeState(
    val totalExpense: Long = 0,
    val expenseLimit: Long = 0,
    val warnMidPercent: Int = MoneyTokens.DefaultWarnMidPercent,
    val warnHighPercent: Int = MoneyTokens.DefaultWarnHighPercent,
    val nowDateYear: String = "",
    val nowDateMonth: String = "",
    val nowDateDayOfMonth: String = "",
    val items: List<Pair<String, List<ExpenseUi>>> = emptyList(),
) {
    val limitOverColor: Color
        get() {
            val progress = if (expenseLimit > 0) {
                totalExpense / expenseLimit.toFloat()
            } else {
                0f
            }
            val midThreshold = warnMidPercent / 100f
            val highThreshold = warnHighPercent / 100f
            return when {
                progress <= midThreshold -> limitsColor_0_50_Color
                progress <= highThreshold -> limitsColor_50_70_Color
                else -> limitsColor_100_Color
            }
        }
}
