package core.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import app.presentation.AppTheme
import core.ui.MoneyTheme
import core.ui.MoneyTokens
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.cd_delete
import moneymanagerkmp.composeapp.generated.resources.food_coffee
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * D3 CategoryIconTile:
 * 52dp rounded square (16dp radius) with 26dp icon.
 * Selected state applies 2dp colored border + subtle elevation ONLY to the icon box, leaving text unframed.
 */
@Composable
fun CategoryIconTile(
    icon: Painter?,
    label: String,
    selected: Boolean,
    selectionColor: Color,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    showLabel: Boolean = true,
    showDeleteBadge: Boolean = false,
    onDeleteClick: () -> Unit = {},
) {
    val tileShape = RoundedCornerShape(14.dp)
    Column(
        modifier = modifier
            .clip(tileShape)
            .then(
                if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
            )
            .padding(vertical = 8.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(7.dp),
    ) {
        Box {
            Surface(
                modifier = Modifier.size(MoneyTokens.CategoryBoxSize),
                shape = RoundedCornerShape(MoneyTokens.CategoryBoxRadius),
                color = MoneyTheme.colors.surface,
                border = BorderStroke(
                    width = if (selected) 2.dp else 1.5.dp,
                    color = if (selected) selectionColor else MoneyTheme.colors.line,
                ),
                shadowElevation = if (selected) 3.dp else 0.dp,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    icon?.let { painter ->
                        Icon(
                            modifier = Modifier.size(MoneyTokens.CategoryIconSize),
                            painter = painter,
                            contentDescription = if (showLabel) null else label,
                            tint = MoneyTheme.colors.ink,
                        )
                    }
                }
            }
            if (showDeleteBadge) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(MoneyTheme.colors.surfaceAlt)
                        .clickable(onClick = onDeleteClick),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        modifier = Modifier.size(12.dp),
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(Res.string.cd_delete),
                        tint = MoneyTheme.colors.muted,
                    )
                }
            }
        }

        if (showLabel) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                ),
                color = if (selected) MoneyTheme.colors.ink else MoneyTheme.colors.muted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview
@Composable
private fun CategoryIconTileLightPreview() {
    AppTheme(darkTheme = false) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            CategoryIconTile(
                icon = painterResource(Res.drawable.food_coffee),
                label = "飲料",
                selected = false,
                selectionColor = MoneyTokens.CatFood,
            )
            CategoryIconTile(
                icon = painterResource(Res.drawable.food_coffee),
                label = "咖啡",
                selected = true,
                selectionColor = MoneyTokens.CatFood,
            )
            CategoryIconTile(
                icon = painterResource(Res.drawable.food_coffee),
                label = "咖啡",
                selected = false,
                selectionColor = MoneyTokens.CatFood,
                showDeleteBadge = true,
            )
        }
    }
}

@Preview
@Composable
private fun CategoryIconTileDarkPreview() {
    AppTheme(darkTheme = true) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            CategoryIconTile(
                icon = painterResource(Res.drawable.food_coffee),
                label = "飲料",
                selected = false,
                selectionColor = MoneyTokens.CatFood,
            )
            CategoryIconTile(
                icon = painterResource(Res.drawable.food_coffee),
                label = "咖啡",
                selected = true,
                selectionColor = MoneyTokens.CatFood,
            )
        }
    }
}
