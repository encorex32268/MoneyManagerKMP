package feature.core.domain.mapper

import feature.core.data.model.ExpenseEntity
import feature.core.domain.model.Category
import feature.core.domain.model.Expense

fun Expense.toExpenseEntity() : ExpenseEntity {

    return ExpenseEntity(
        id = id,
        typeId = typeId,
        categoryId = categoryId,
        description = description,
        isIncome = isIncome,
        cost = cost,
        timestamp = timestamp)
}

fun ExpenseEntity.toExpense() : Expense {
    return Expense(
        id = id,
        typeId = typeId,
        categoryId = categoryId,
        description = description,
        isIncome = isIncome,
        cost = cost,
        timestamp = timestamp)
}