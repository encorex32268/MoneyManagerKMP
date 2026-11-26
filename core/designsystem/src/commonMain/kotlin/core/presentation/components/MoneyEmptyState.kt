package core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import app.presentation.AppTheme
import core.ui.MoneyTheme
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.baseline_pie_chart_24_outline
import moneymanagerkmp.composeapp.generated.resources.baseline_receipt_24_outline
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * D3 · Ring & Clarity Empty State Card for screens with no data.
 */
@Composable
fun MoneyEmptyState(
    icon: Painter,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null,
) {
    MoneyCard(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 32.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .background(
                        color = MoneyTheme.colors.surfaceAlt,
                        shape = CircleShape,
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .background(
                            color = MoneyTheme.colors.bg,
                            shape = CircleShape,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        modifier = Modifier.size(26.dp),
                        painter = icon,
                        contentDescription = null,
                        tint = MoneyTheme.colors.muted,
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MoneyTheme.colors.ink,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MoneyTheme.colors.muted,
                textAlign = TextAlign.Center,
            )

            if (actionLabel != null && onActionClick != null) {
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    modifier = Modifier.height(44.dp),
                    shape = RoundedCornerShape(14.dp),
                    contentPadding = PaddingValues(horizontal = 22.dp, vertical = 10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MoneyTheme.colors.accent,
                        contentColor = MoneyTheme.colors.accentFg,
                    ),
                    onClick = onActionClick,
                ) {
                    Text(
                        text = actionLabel,
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun MoneyEmptyStateWithActionPreview() {
    AppTheme(darkTheme = false) {
        MoneyEmptyState(
            modifier = Modifier.padding(16.dp),
            icon = painterResource(Res.drawable.baseline_receipt_24_outline),
            title = "本月尚無支出紀錄",
            subtitle = "點擊下方按鈕，輕鬆記下本月的第一筆花費。",
            actionLabel = "記一筆支出",
            onActionClick = {},
        )
    }
}

@Preview
@Composable
private fun MoneyEmptyStateNoActionPreview() {
    AppTheme(darkTheme = false) {
        MoneyEmptyState(
            modifier = Modifier.padding(16.dp),
            icon = painterResource(Res.drawable.baseline_pie_chart_24_outline),
            title = "本月尚無消費分布",
            subtitle = "新增支出或切換月份，即可查看各群組的花費比例。",
        )
    }
}

@Preview
@Composable
private fun MoneyEmptyStateDarkPreview() {
    AppTheme(darkTheme = true) {
        MoneyEmptyState(
            modifier = Modifier.padding(16.dp),
            icon = painterResource(Res.drawable.baseline_receipt_24_outline),
            title = "本月尚無支出紀錄",
            subtitle = "點擊下方按鈕，輕鬆記下本月的第一筆花費。",
            actionLabel = "記一筆支出",
            onActionClick = {},
        )
    }
}

@Preview
@Composable
private fun MoneyEmptyStateLargeFontPreview() {
    val density = LocalDensity.current
    CompositionLocalProvider(
        LocalDensity provides Density(density = density.density, fontScale = 2f)
    ) {
        AppTheme(darkTheme = false) {
            MoneyEmptyState(
                modifier = Modifier.padding(16.dp),
                icon = painterResource(Res.drawable.baseline_receipt_24_outline),
                title = "本月尚無支出紀錄",
                subtitle = "點擊下方按鈕，輕鬆記下本月的第一筆花費。",
                actionLabel = "記一筆支出",
                onActionClick = {},
            )
        }
    }
}
