package feature.core.domain.mapper

import feature.core.data.model.ExpenseEntity
import feature.core.domain.model.Expense

fun Expense.toExpenseEntity() : ExpenseEntity {
    return ExpenseEntity(
        category = category,
        description = description,
        isIncome = isIncome,
        cost = cost,
        timestamp = timestamp
    )
}

fun ExpenseEntity.toExpense() : Expense {
    return Expense(
        category = category,
        description = description,
        isIncome = isIncome,
        cost = cost,
        timestamp = timestamp
    )
}