package core.domain.mapper

import core.data.model.ExpenseEntity
import core.domain.model.Expense

fun Expense.toExpenseEntity() : ExpenseEntity {

    return ExpenseEntity(
        id = id,
        typeId = typeId,
        categoryId = categoryId,
        description = description,
        isIncome = isIncome,
        cost = cost,
        timestamp = timestamp,
        content = content
    )
}

fun ExpenseEntity.toExpense() : Expense {
    return Expense(
        id = id,
        typeId = typeId,
        categoryId = categoryId,
        description = description,
        isIncome = isIncome,
        cost = cost,
        timestamp = timestamp,
        content = content
    )
}