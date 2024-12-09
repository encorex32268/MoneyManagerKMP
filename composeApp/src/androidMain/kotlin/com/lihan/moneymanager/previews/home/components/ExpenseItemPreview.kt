package com.lihan.moneymanager.previews.home.components

import AppTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import feature.core.domain.model.Expense
import feature.core.presentation.CategoryList
import feature.core.presentation.components.DatePicker
import feature.home.presentation.components.ExpenseItem

@Preview(showSystemUi = true)
@Composable
fun ExpenseItemPreview(){
    AppTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            ExpenseItem(
                items = listOf(
                    Expense(
                        categoryId = CategoryList.items.random().id,
                        typeId = CategoryList.HEALTH,
                        description = "Tester",
                        content = "123",
                        cost = 100
                    ),
                    Expense(
                        categoryId = CategoryList.items.random().id,
                        typeId = CategoryList.FOOD,
                        description = "Tester2",
                        content = "",
                        cost = 300
                    )
                )
            )
        }
    }
}