@file:OptIn(ExperimentalResourceApi::class, ExperimentalResourceApi::class)

package com.lihan.moneymanager.previews.chart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import app.presentation.AppTheme
import core.domain.model.Expense
import core.domain.model.Type
import core.presentation.CategoryList
import feature.chart.presentation.ChartScreen
import feature.chart.presentation.ChartState
import feature.chart.presentation.components.ExpenseDetailLazyGrid
import org.jetbrains.compose.resources.ExperimentalResourceApi


@Preview(showSystemUi = true)
@Composable
fun ChartScreenPreview() {

    val categories = CategoryList.items.slice(IntRange(0 , 10))
    val chart = core.domain.model.chart.Chart(
        type = Type(
            typeIdTimestamp = 1,
            name = "Food",
            colorArgb = Color.Red.toArgb(),
            order = 1,
            isShow = true,
            categories = CategoryList.items.slice(IntRange(0, 10))
        ),
        items = (0..10).map {
            Expense(
                categoryId = categories.random().id,
                typeId = 1,
                description = "description ${it}",
                cost = 100 * it.toLong(),
            )
        }
    )


    AppTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .statusBarsPadding()
        ){
            ChartScreen(
                state = ChartState(
                    nowDateYear = "2024",
                    nowDateMonth = "11",
                    items = listOf(chart),
                    totalExpense = 30000,
                    spendingLimit = 60000
                ),
                isDebug = true
            )

        }
    }
}


@Preview
@Composable
fun ExpenseDetailLayoutPreview() {

    AppTheme {
        ExpenseDetailLazyGrid(
            items = listOf(),
            sumTotal = 100L,
            onItemClick = {}
        )
    }
}