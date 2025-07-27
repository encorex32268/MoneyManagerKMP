package com.lihan.moneymanager.previews.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import app.presentation.AppTheme
import core.domain.FOOD
import core.domain.TRAFFIC
import core.domain.model.Expense
import core.presentation.CategoryList
import feature.home.domain.mapper.toExpenseUi
import feature.home.presentation.edit.EditExpenseScreen
import feature.home.presentation.edit.EditExpenseState

@Preview
@Composable
fun EditExpenseScreenPreview() {
    AppTheme {
        EditExpenseScreen(
            state = EditExpenseState(
                currentExpenseUi = Expense(
                    categoryId = (FOOD + 1) .toInt(),
                    typeId = FOOD,
                    description = "Edit Description",
                    cost = 770L,
                    content = "This is content"
                ).toExpenseUi()
            ),
            onEvent = {}
        )
    }
}

@Preview
@Composable
fun EditExpenseScreenIncomePreview() {
    AppTheme {
        EditExpenseScreen(
            state = EditExpenseState(
                currentExpenseUi = Expense(
                    categoryId = (TRAFFIC + 1) .toInt(),
                    typeId = TRAFFIC,
                    description = "Edit Description",
                    cost = 770L,
                    content = "This is content",
                    isIncome =  true
                ).toExpenseUi()
            )
        )
    }
}

