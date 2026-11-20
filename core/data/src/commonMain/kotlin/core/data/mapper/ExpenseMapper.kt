package core.domain.mapper

import core.data.model.ExpenseEntity
import core.domain.model.Expense

fun ExpenseEntity.toExpense(): Expense {
    val hexId = id.toHexString()
    return Expense(
        id = hexId,
        idString = hexId,
        typeId = typeId,
        categoryId = categoryId,
        description = description,
        isIncome = isIncome,
        cost = cost,
        timestamp = timestamp,
        content = content
    )
}
