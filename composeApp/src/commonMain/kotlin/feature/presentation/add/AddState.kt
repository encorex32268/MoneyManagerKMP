package feature.presentation.add

import feature.core.domain.mapper.toCategoryUi
import feature.core.domain.model.Expense
import feature.core.presentation.CategoryList
import feature.core.presentation.model.CategoryUi
import kotlinx.datetime.LocalDateTime

data class AddState(
    val categoryItems: List<CategoryUi> = CategoryList.items.map { it.toCategoryUi() },
    val recentlyCategoryItems: List<CategoryUi> = emptyList(),
    val currentExpense: Expense?=null,
    val categoryUi: CategoryUi?=null,
    val description: String = "",
    val isIncome: Boolean = false,
    val cost: String = "",
    val year: Int = 0,
    val monthNumber: Int = 0,
    val dayOfMonth: Int = 0,
    val nowLocalDateTime: LocalDateTime?=null
)
