package core.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.presentation.AppTheme
import core.ui.MoneyTheme
import core.ui.MoneyTokens
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * D3 GroupChip: Selected state changes ONLY border color & text emphasis (no solid dark fill).
 */
@Composable
fun GroupChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    dotColor: Color? = null,
) {
    val activeBorderColor = dotColor ?: MoneyTheme.colors.ink
    FilterChip(
        selected = selected,
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(MoneyTokens.ChipRadius),
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold,
                ),
            )
        },
        leadingIcon = dotColor?.let { color ->
            {
                Box(
                    modifier = Modifier
                        .size(9.dp)
                        .background(color = color, shape = CircleShape),
                )
            }
        },
        colors = FilterChipDefaults.filterChipColors(
            containerColor = MoneyTheme.colors.surface,
            labelColor = MoneyTheme.colors.muted,
            selectedContainerColor = MoneyTheme.colors.surface,
            selectedLabelColor = MoneyTheme.colors.ink,
        ),
        border = BorderStroke(
            width = if (selected) 1.8.dp else 1.5.dp,
            color = if (selected) activeBorderColor else MoneyTheme.colors.line,
        ),
    )
}

@Preview
@Composable
private fun GroupChipLightPreview() {
    AppTheme(darkTheme = false) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            GroupChip(label = "最近", selected = false, onClick = {})
            GroupChip(label = "生活", selected = false, onClick = {}, dotColor = MoneyTokens.CatLife)
            GroupChip(label = "食", selected = true, onClick = {}, dotColor = MoneyTokens.CatFood)
        }
    }
}

@Preview
@Composable
private fun GroupChipDarkPreview() {
    AppTheme(darkTheme = true) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            GroupChip(label = "最近", selected = true, onClick = {})
            GroupChip(label = "食", selected = true, onClick = {}, dotColor = MoneyTokens.CatFood)
        }
    }
}
