@file:OptIn(ExperimentalResourceApi::class)

package com.lihan.moneymanager.previews.chart

import AppTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import feature.chart.presentation.ChartScreen
import feature.chart.presentation.ChartState
import feature.chart.presentation.components.ExpenseDetailLayout
import feature.core.domain.model.Expense
import feature.core.domain.model.Type
import feature.core.domain.model.chart.Chart
import feature.core.presentation.CategoryList
import org.jetbrains.compose.resources.ExperimentalResourceApi


@Preview
@Composable
fun ChartScreenPreview() {

    val categories = CategoryList.items.slice(IntRange(0 , 10))
    val chart = Chart(
        type = Type(
            typeIdTimestamp = 1,
            name = "Food",
            colorArgb = Color.Red.toArgb(),
            order = 1,
            isShow = true,
            categories = CategoryList.items.slice(IntRange(0 , 10))
        ),
        items = (0..10).map {
            Expense(
                categoryId = categories.random().id,
                typeId = 1,
                description = "description ${it}",
                cost = 100 * it .toLong(),
            )
        }
    )


    AppTheme {
        ChartScreen(
            state = ChartState(
                nowDateYear = "2024",
                nowDateMonth = "11",
                items = listOf(chart)
            ),
            isDebug = true
        )
    }
}


@Preview
@Composable
fun ExpenseDetailLayoutPreview() {

    AppTheme {
        ExpenseDetailLayout(
            items = listOf(),
            sumTotal = 100L,
            onItemClick = {}
        )
    }
}