package feature.home.presentation.add

import core.domain.mapper.toCategoryUi
import core.domain.model.Expense
import core.domain.model.Type
import core.presentation.CategoryList
import core.presentation.model.CategoryUi
import feature.home.presentation.add.type.TypeUi
import kotlinx.datetime.LocalDateTime


data class AddState(
    val isLoading: Boolean = true,
    val types: List<TypeUi> = emptyList(),
    val recentlyItems: TypeUi?=null,
    val currentExpense: Expense?=null,
    val categoryUi: CategoryUi?=null,
    val description: String = "",
    val cost: String = "",
    val year: Int = 0,
    val monthNumber: Int = 0,
    val dayOfMonth: Int = 0,
    val nowLocalDateTime: LocalDateTime?=null
)
