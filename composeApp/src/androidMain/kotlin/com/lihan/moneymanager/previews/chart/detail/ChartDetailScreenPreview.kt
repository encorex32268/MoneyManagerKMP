package com.lihan.moneymanager.previews.chart.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import app.presentation.AppTheme
import core.domain.FOOD
import core.domain.model.Expense
import core.domain.model.Type
import feature.chart.presentation.chartdetail.DetailScreen
import feature.chart.presentation.chartdetail.DetailState

@Preview(showSystemUi = true)
@Composable
fun ChartDetailScreenPreview() {
    val expenses = remember {
        listOf(
            "11/21" to (0..10).map {
                Expense(
                    categoryId = FOOD.toInt() + it,
                    typeId =FOOD,
                    description = "Test - ${it}",
                    cost = (1000 * it).toLong(),
                    content = if (it%2 == 0)"1234" else ""
                )
            }
        )

    }
    AppTheme {
        Box(modifier = Modifier.fillMaxSize().background(Color.White)){
            DetailScreen(
                state = DetailState(
                    total = 3300,
                    type = Type(
                        typeIdTimestamp = 1,
                        name = "Food",
                        colorArgb = Color.Magenta.toArgb(),
                        order = 1
                    ),
                    items = expenses,
                )
            )

        }
    }
}
