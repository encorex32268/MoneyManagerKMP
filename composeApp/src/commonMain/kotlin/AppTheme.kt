import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import feature.core.ui.darkScheme
import feature.core.ui.lightScheme
import feature.core.ui.getTypography


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
