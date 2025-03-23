package feature.home.presentation

import core.domain.model.Expense
import core.domain.model.Type

data class HomeState(
    val totalExpense: Long = 0,
    val expenseLimit: Long = 0,
    val items:  List<Pair<String, List<Expense>>> = emptyList(),
    val nowDateYear: String = "",
    val nowDateMonth: String = "",
    val nowDateDayOfMonth: String = "",
    val typesItem: List<Type> = emptyList()
)
