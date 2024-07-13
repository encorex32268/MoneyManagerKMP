package feature.core.domain.model.chart

import feature.core.domain.model.Expense

data class Chart(
    val typeId: Int,
    val expenseItems: List<Expense> = emptyList()
//    val typeId: Int,
//    val color: Color = CategoryList.getColorByCategory(typeId),
//    val income: Long = 0L,
//    val expense: Long = 0L,
//    val detailItems: List<Expense> = emptyList()
)
