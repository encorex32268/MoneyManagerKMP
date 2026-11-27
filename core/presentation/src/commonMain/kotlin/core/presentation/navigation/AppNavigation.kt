package core.presentation.navigation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.presentation.AppTheme
import core.ui.MoneyTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.tooling.preview.Preview

/**
 * D3 · Ring & Clarity Floating Pill Bottom Navigation (Android & iOS).
 */
@Composable
fun AppNavigationBottom(
    itemSelectedIndex: Int,
    onBarItemClick: (Int, String) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier,
) {
    val activeColor = MoneyTheme.colors.ink
    val inactiveColor = MoneyTheme.colors.muted

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center,
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 360.dp)
                .height(62.dp),
            shape = CircleShape,
            color = MoneyTheme.colors.surface,
            border = BorderStroke(1.dp, MoneyTheme.colors.cardBorder),
            shadowElevation = 8.dp,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                bottomNavigationItems.forEachIndexed { index, bottomNavigationItem ->
                    val isSelected = itemSelectedIndex == index
                    val title = stringResource(bottomNavigationItem.title)
                    val itemColor = if (isSelected) activeColor else inactiveColor

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .clip(CircleShape)
                            .background(
                                color = if (isSelected) MoneyTheme.colors.surfaceAlt else Color.Transparent,
                                shape = CircleShape,
                            )
                            .clickable {
                                onBarItemClick(index, bottomNavigationItem.name)
                            },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Icon(
                            modifier = Modifier.size(20.dp),
                            painter = painterResource(
                                if (isSelected) {
                                    bottomNavigationItem.selectedIcon
                                } else {
                                    bottomNavigationItem.unSelectedIcon
                                }
                            ),
                            contentDescription = title,
                            tint = itemColor,
                        )
                        Text(
                            text = title,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            ),
                            color = itemColor,
                            maxLines = 1,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AppNavigationRail(
    itemSelectedIndex: Int,
    onBarItemClick: (Int, String) -> Unit = { _, _ -> },
) {
    val activeColor = MoneyTheme.colors.ink
    val inactiveColor = MoneyTheme.colors.muted

    NavigationRail(
        modifier = Modifier.width(72.dp),
        containerColor = MoneyTheme.colors.surface,
    ) {
        bottomNavigationItems.forEachIndexed { index, bottomNavigationItem ->
            val isSelected = itemSelectedIndex == index
            val title = stringResource(bottomNavigationItem.title)
            NavigationRailItem(
                colors = NavigationRailItemDefaults.colors(
                    indicatorColor = MoneyTheme.colors.surfaceAlt,
                    selectedIconColor = activeColor,
                    selectedTextColor = activeColor,
                    unselectedIconColor = inactiveColor,
                    unselectedTextColor = inactiveColor,
                ),
                selected = isSelected,
                onClick = {
                    onBarItemClick(index, bottomNavigationItem.name)
                },
                icon = {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(
                            if (isSelected) {
                                bottomNavigationItem.selectedIcon
                            } else {
                                bottomNavigationItem.unSelectedIcon
                            }
                        ),
                        contentDescription = title,
                    )
                },
                label = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelSmall,
                    )
                },
            )
        }
    }
}

@Preview
@Composable
private fun AppNavigationBottomLightPreview() {
    AppTheme(darkTheme = false) {
        Box(modifier = Modifier.background(MoneyTheme.colors.bg)) {
            AppNavigationBottom(itemSelectedIndex = 0)
        }
    }
}

@Preview
@Composable
private fun AppNavigationBottomDarkPreview() {
    AppTheme(darkTheme = true) {
        Box(modifier = Modifier.background(MoneyTheme.colors.bg)) {
            AppNavigationBottom(itemSelectedIndex = 1)
        }
    }
}
