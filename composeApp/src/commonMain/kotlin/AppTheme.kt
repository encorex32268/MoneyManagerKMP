import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import feature.core.ui.lightScheme
import ui.getTypography


@Composable
fun AppTheme(
    content : @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = lightScheme,
        content = content,
        typography = getTypography()
    )
}
