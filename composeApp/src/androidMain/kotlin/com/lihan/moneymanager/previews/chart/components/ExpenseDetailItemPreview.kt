package com.lihan.moneymanager.previews.chart.components

import AppTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import feature.chart.presentation.components.ExpenseDetailItem
import feature.core.domain.model.Expense
import feature.core.domain.model.Type
import feature.core.domain.model.chart.Chart
import feature.core.presentation.CategoryList


@Preview
@Composable
fun ExpenseDetailItemPreview() {
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
            ,
            contentAlignment = Alignment.Center
        ){
            ExpenseDetailItem(
                chart = chart,
                percent = 1.0f,
                sum = 200
            )
        }
    }
}