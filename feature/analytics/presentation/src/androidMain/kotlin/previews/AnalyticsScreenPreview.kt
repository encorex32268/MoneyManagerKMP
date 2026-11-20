package com.lihan.moneymanager.previews.analytics

import app.presentation.AppTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import feature.analytics.presentation.AnalyticsScreen
import feature.analytics.presentation.AnalyticsState


@Preview
@Composable
fun AnalyticsScreenPreview() {
    AppTheme {
        Box(
            modifier = Modifier.fillMaxSize()
        ){
            AnalyticsScreen(
                state = AnalyticsState()
            )
        }
    }

}