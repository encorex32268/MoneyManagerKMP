package com.lihan.moneymanager.previews.home

import AppTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import feature.core.domain.model.Expense
import feature.core.presentation.CategoryList
import feature.core.presentation.model.CategoryUi
import feature.home.presentation.HomeScreen
import feature.home.presentation.HomeState

@androidx.compose.runtime.Composable
@androidx.compose.ui.tooling.preview.Preview(
    showSystemUi = true
)
fun HomeScreenPreview() {
    val expenses = remember {
        listOf(
            "11/21" to (0..10).map {
                Expense(
                    categoryId = CategoryList.FOOD.toInt() + it,
                    typeId = CategoryList.FOOD,
                    description = "Test - ${it}",
                    cost = (1000 * it).toLong(),
                    content = if (it%2 == 0)"1234" else ""
                )
            }
        )

    }
    AppTheme{
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ){
           HomeScreen(
                state = HomeState(
                    income = 100,
                    expense = 200,
                    totalAmount = 300,
                    nowDateYear = "2024",
                    nowDateMonth = "11",
                    nowDateDayOfMonth = "21",
                    items =  expenses
                ),
                onEvent = {},
               isDebug = true
           )
        }

    }
}
