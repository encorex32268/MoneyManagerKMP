package com.lihan.moneymanager.previews.home.components

import app.presentation.AppTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import feature.home.presentation.components.AmountText

@Preview(showSystemUi = true)
@Composable
fun AmountTextPreview(){
    AppTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            verticalArrangement = Arrangement.spacedBy(16.dp,Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            AmountText(
                title = "Total",
                text = "￥1,234,56",
            )
            AmountText(
                title = "Testerqweqweew",
                text = "￥1,234,56,13213123",
            )
        }
    }
}