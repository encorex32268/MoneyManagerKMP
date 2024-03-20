package feature.home.presentation

import feature.core.domain.model.Expense

data class HomeState(
    val items: List<Expense> = emptyList()
)
