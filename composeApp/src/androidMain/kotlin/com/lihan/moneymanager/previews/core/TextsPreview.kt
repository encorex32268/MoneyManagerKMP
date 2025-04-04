package com.lihan.moneymanager.previews.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import app.presentation.AppTheme
import core.presentation.Texts

@Composable
fun TextsPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ){
        Texts.BodyLarge(text = "BodyLarge 100000")
        Texts.BodyMedium(text = "BodyMedium 233000")
        Texts.BodySmall(text = "BodySmall 124556")
        Texts.DisplaySmall(text = "DisplaySmall 110")
        Texts.TitleLarge(text = "TitleLarge 12345")
        Texts.TitleMedium(text = "TitleMedium 12345")
        Texts.TitleSmall(text = "TitleSmall 12345")
    }

}

@Preview(showSystemUi = true)
@Composable
fun TextsPreviewPreview() {
    AppTheme {
        TextsPreview()
    }

}