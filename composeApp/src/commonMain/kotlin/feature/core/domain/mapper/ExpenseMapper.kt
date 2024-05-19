package feature.core.domain.mapper

import feature.core.data.model.ExpenseEntity
import feature.core.domain.model.Category
import feature.core.domain.model.Expense

fun Expense.toExpenseEntity() : ExpenseEntity {

    return ExpenseEntity().apply {
        timestamp = this@toExpenseEntity.timestamp
        typeId = this@toExpenseEntity.typeId
        categoryId = this@toExpenseEntity.categoryId
        description = this@toExpenseEntity.description
        isIncome = this@toExpenseEntity.isIncome
        cost = this@toExpenseEntity.cost
    }
}

fun ExpenseEntity.toExpense() : Expense {
    return Expense(
        typeId = typeId,
        categoryId = categoryId,
        description = description,
        isIncome = isIncome,
        cost = cost,
        timestamp = timestamp
    )
}