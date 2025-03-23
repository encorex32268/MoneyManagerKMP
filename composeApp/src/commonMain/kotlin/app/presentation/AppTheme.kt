package app.presentation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import core.ui.darkScheme
import core.ui.getTypography
import core.ui.lightScheme

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content : @Composable () -> Unit
) {
    val colorScheme = if (darkTheme){
        darkScheme
    }else{
        lightScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content,
        typography = getTypography()
    )
}
