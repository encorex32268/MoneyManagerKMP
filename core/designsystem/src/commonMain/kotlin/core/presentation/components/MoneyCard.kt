package core.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import app.presentation.AppTheme
import core.ui.MoneyTheme
import core.ui.MoneyTokens
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MoneyCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    shape: Shape = RoundedCornerShape(MoneyTokens.CardRadius),
    containerColor: Color = MoneyTheme.colors.surface,
    borderColor: Color = MoneyTheme.colors.cardBorder,
    shadowElevation: Dp = MoneyTokens.CardElevation,
    contentPadding: PaddingValues = PaddingValues(18.dp),
    content: @Composable ColumnScope.() -> Unit,
) {
    val border = BorderStroke(1.dp, borderColor)
    if (onClick != null) {
        Surface(
            onClick = onClick,
            modifier = modifier,
            shape = shape,
            color = containerColor,
            contentColor = MoneyTheme.colors.ink,
            border = border,
            shadowElevation = shadowElevation,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(contentPadding),
                content = content,
            )
        }
    } else {
        Surface(
            modifier = modifier,
            shape = shape,
            color = containerColor,
            contentColor = MoneyTheme.colors.ink,
            border = border,
            shadowElevation = shadowElevation,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(contentPadding),
                content = content,
            )
        }
    }
}

@Preview
@Composable
private fun MoneyCardLightPreview() {
    AppTheme(darkTheme = false) {
        MoneyCard(modifier = Modifier.padding(16.dp)) {
            Text(text = "MoneyCard Light", style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Preview
@Composable
private fun MoneyCardDarkPreview() {
    AppTheme(darkTheme = true) {
        MoneyCard(modifier = Modifier.padding(16.dp)) {
            Text(text = "MoneyCard Dark", style = MaterialTheme.typography.titleMedium)
        }
    }
}
