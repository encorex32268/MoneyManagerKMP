package com.lihan.moneymanager.previews.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import app.presentation.AppTheme
import core.ui.getTypography

@Composable
fun TextsPreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center
    ){
        Text(
            text = "Hello word 12345 g",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Normal
        )
        Text(
            text = "BodyMedium 233000",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "BodyLarge 100000",
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = "BodyLarge 100000",
            style = MaterialTheme.typography.displayLarge
        )
        Text(
            text = "BodyLarge 100000",
            style = MaterialTheme.typography.displayMedium
        )
        Text(
            text = "BodyLarge 100000",
            style = MaterialTheme.typography.displaySmall
        )
        Text(
            text = "BodyLarge 100000",
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = "BodyLarge 100000",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "BodyLarge 100000",
            style = MaterialTheme.typography.titleSmall
        )
    }

}

@Preview(showSystemUi = true)
@Composable
fun TextsPreviewPreview() {
    AppTheme {
        TextsPreview()
    }
}