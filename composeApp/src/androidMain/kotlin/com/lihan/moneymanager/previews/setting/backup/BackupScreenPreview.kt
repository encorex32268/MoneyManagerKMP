package com.lihan.moneymanager.previews.setting.backup

import app.presentation.AppTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import feature.analytics.presentation.backup.BackupScreen

@Preview(showSystemUi = true)
@Composable
fun BackupScreenPreview(){
    AppTheme {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.White)){
            BackupScreen()
        }

    }
}