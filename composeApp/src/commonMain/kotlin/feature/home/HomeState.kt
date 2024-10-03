package feature.home

import feature.core.domain.model.Expense
import feature.core.domain.model.Type

data class HomeState(
    val income: Long = 0,
    val expense: Long = -0,
    val totalAmount: Long = income + -expense,
    val items:  List<Pair<String, List<Expense>>> = emptyList(),
    val nowDateYear: String = "",
    val nowDateMonth: String = "",
    val nowDateDayOfMonth: String = "",
    val typesItem: List<Type> = emptyList()
)
