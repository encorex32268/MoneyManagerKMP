package com.lihan.moneymanager.previews.home.components

import app.presentation.AppTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import core.presentation.components.DatePicker

@Preview(showSystemUi = true)
@Composable
fun DatePickerPreview(){
    AppTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ){
            DatePicker(
                year = 2024,
                month =12,
                onDateChange = { _ , _ ->

                }
            )
        }
    }
}