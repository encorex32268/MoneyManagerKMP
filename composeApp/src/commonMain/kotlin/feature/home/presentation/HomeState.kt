package feature.home.presentation

import androidx.compose.ui.graphics.Color
import core.domain.model.Expense
import core.domain.model.Type
import core.ui.limitsColor_0_50_Color
import core.ui.limitsColor_100_Color
import core.ui.limitsColor_50_70_Color
import core.ui.limitsColor_70_90_Color

data class HomeState(
    val totalExpense: Long = 0,
    val expenseLimit: Long = 0,
    val items:  List<Pair<String, List<Expense>>> = emptyList(),
    val nowDateYear: String = "",
    val nowDateMonth: String = "",
    val nowDateDayOfMonth: String = "",
    val typesItem: List<Type> = emptyList()
){
    val limitOverColor: Color
        get() {
            val progress = if (expenseLimit > 0) {
                totalExpense / expenseLimit.toFloat()
            } else {
                0f
            }
            return  when(progress){
                in 0.0f..0.5f -> limitsColor_0_50_Color
                in 0.5f..0.7f -> limitsColor_50_70_Color
                in 0.7f..0.9f -> limitsColor_70_90_Color
                else -> limitsColor_100_Color
            }
        }

}
