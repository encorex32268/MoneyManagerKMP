package app.presentation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import core.ui.DarkMoneyColors
import core.ui.LightMoneyColors
import core.ui.LocalMoneyColors
import core.ui.darkScheme
import core.ui.getTypography
import core.ui.lightScheme

val LocalDarkLightMode = compositionLocalOf { false }

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) darkScheme else lightScheme
    val moneyColors = if (darkTheme) DarkMoneyColors else LightMoneyColors

    CompositionLocalProvider(
        LocalMoneyColors provides moneyColors,
        LocalDarkLightMode provides darkTheme,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = getTypography(),
            content = content,
        )
    }
}
