package feature.home.domain.mapper

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import core.domain.model.Expense
import core.presentation.CategoryList
import feature.home.presentation.model.ExpenseUi

fun Expense.toExpenseUi(): ExpenseUi{
    return ExpenseUi(
        id = id,
        typeId = typeId,
        categoryId = categoryId,
        description = description,
        cost = cost,
        timestamp = timestamp,
        content = content,
        type = type
    )
}
fun ExpenseUi.toExpense(): Expense {
    return Expense(
        id = id,
        typeId = typeId,
        categoryId = categoryId,
        description = description,
        cost = cost,
        timestamp = timestamp,
        content = content,
        type = type
    )
}