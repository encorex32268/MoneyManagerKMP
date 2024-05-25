package feature.chart.domain.model

import androidx.compose.ui.graphics.Color
import feature.core.domain.model.Expense
import feature.core.presentation.CategoryList

data class Chart(
    val typeId: Int,
    val expenseItems: List<Expense> = emptyList()
//    val typeId: Int,
//    val color: Color = CategoryList.getColorByCategory(typeId),
//    val income: Long = 0L,
//    val expense: Long = 0L,
//    val detailItems: List<Expense> = emptyList()
)
