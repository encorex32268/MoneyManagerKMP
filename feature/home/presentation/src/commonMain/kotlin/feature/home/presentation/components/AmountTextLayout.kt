package feature.home.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.presentation.AppTheme
import core.presentation.navigation.NavigationLayoutType
import core.ui.limitsColor_0_50_Color
import core.ui.limitsColor_100_Color
import format
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.home_spending_limit
import moneymanagerkmp.composeapp.generated.resources.total_expense
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import toMoneyString

private val SummaryCardShape = RoundedCornerShape(16.dp)

@Composable
fun AmountTextLayout(
    modifier: Modifier = Modifier,
    totalExpense: Long,
    expenseLimit: Long,
    limitOverColor: Color,
    navigationLayoutType: NavigationLayoutType = NavigationLayoutType.BOTTOM_NAVIGATION,
    onExpenseLimitClick: () -> Unit = {},
) {
    when (navigationLayoutType) {
        NavigationLayoutType.BOTTOM_NAVIGATION -> {
            AmountSummaryCardBottom(
                modifier = modifier,
                totalExpense = totalExpense,
                expenseLimit = expenseLimit,
                limitOverColor = limitOverColor,
                onExpenseLimitClick = onExpenseLimitClick,
            )
        }
        else -> {
            AmountSummaryCardRail(
                modifier = modifier,
                totalExpense = totalExpense,
                expenseLimit = expenseLimit,
                limitOverColor = limitOverColor,
                onExpenseLimitClick = onExpenseLimitClick,
            )
        }
    }
}

@Composable
private fun AmountSummaryCardBottom(
    totalExpense: Long,
    expenseLimit: Long,
    limitOverColor: Color,
    onExpenseLimitClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val expenseColor = if (expenseLimit > 0L) {
        limitOverColor
    } else {
        MaterialTheme.colorScheme.onSurface
    }
    val limitColor = if (expenseLimit > 0L) {
        MaterialTheme.colorScheme.onSurface
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Surface(
        modifier = modifier,
        shape = SummaryCardShape,
        color = MaterialTheme.colorScheme.surfaceContainerLowest,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shadowElevation = 2.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 4.dp, horizontal = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = stringResource(Res.string.total_expense),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = totalExpense.toMoneyString(),
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                        ),
                        color = expenseColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                VerticalDivider(
                    modifier = Modifier
                        .padding(vertical = 6.dp)
                        .width(1.dp),
                    color = MaterialTheme.colorScheme.outlineVariant,
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable(onClick = onExpenseLimitClick)
                        .padding(vertical = 4.dp, horizontal = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = stringResource(Res.string.home_spending_limit),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            modifier = Modifier.size(13.dp),
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = expenseLimit.toMoneyString(),
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center,
                        ),
                        color = limitColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            if (expenseLimit > 0L) {
                Spacer(modifier = Modifier.height(12.dp))
                LimitProgressRow(
                    totalExpense = totalExpense,
                    expenseLimit = expenseLimit,
                    progressColor = limitOverColor,
                )
            }
        }
    }
}

@Composable
private fun AmountSummaryCardRail(
    totalExpense: Long,
    expenseLimit: Long,
    limitOverColor: Color,
    onExpenseLimitClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val expenseColor = if (expenseLimit > 0L) {
        limitOverColor
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    Surface(
        modifier = modifier.padding(8.dp),
        shape = SummaryCardShape,
        color = MaterialTheme.colorScheme.surfaceContainerLowest,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shadowElevation = 2.dp,
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(Res.string.total_expense),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = totalExpense.toMoneyString(),
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    color = expenseColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .clickable(onClick = onExpenseLimitClick)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(Res.string.home_spending_limit),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        modifier = Modifier.size(13.dp),
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = expenseLimit.toMoneyString(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
private fun LimitProgressRow(
    totalExpense: Long,
    expenseLimit: Long,
    progressColor: Color,
    modifier: Modifier = Modifier,
) {
    val progress = remember(totalExpense, expenseLimit) {
        if (expenseLimit > 0L) {
            (totalExpense / expenseLimit.toFloat()).coerceAtLeast(0f)
        } else {
            0f
        }
    }
    val animatable = remember { Animatable(0f) }

    LaunchedEffect(progress) {
        animatable.animateTo(
            targetValue = progress.coerceIn(0f, 1f),
            animationSpec = tween(
                durationMillis = 800,
                easing = LinearOutSlowInEasing,
            ),
        )
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        LinearProgressIndicator(
            modifier = Modifier
                .weight(1f)
                .height(8.dp),
            progress = { animatable.value },
            color = progressColor,
            trackColor = MaterialTheme.colorScheme.surfaceContainerHighest,
            strokeCap = StrokeCap.Round,
            drawStopIndicator = {},
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = "${(progress * 100).toDouble().format(1)}%",
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
            ),
            color = progressColor,
        )
    }
}

@Preview
@Composable
private fun AmountTextLayoutDefaultPreview() {
    AppTheme(darkTheme = false) {
        AmountTextLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            totalExpense = 7639,
            expenseLimit = 20000,
            limitOverColor = limitsColor_0_50_Color,
        )
    }
}

@Preview
@Composable
private fun AmountTextLayoutZeroLimitPreview() {
    AppTheme(darkTheme = false) {
        AmountTextLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            totalExpense = 5100,
            expenseLimit = 0,
            limitOverColor = limitsColor_0_50_Color,
        )
    }
}

@Preview
@Composable
private fun AmountTextLayoutOverLimitPreview() {
    AppTheme(darkTheme = false) {
        AmountTextLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            totalExpense = 23500,
            expenseLimit = 20000,
            limitOverColor = limitsColor_100_Color,
        )
    }
}

@Preview
@Composable
private fun AmountTextLayoutDarkPreview() {
    AppTheme(darkTheme = true) {
        AmountTextLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            totalExpense = 7639,
            expenseLimit = 20000,
            limitOverColor = limitsColor_0_50_Color,
        )
    }
}

@Preview
@Composable
private fun AmountTextLayoutRailPreview() {
    AppTheme(darkTheme = false) {
        AmountTextLayout(
            totalExpense = 7639,
            expenseLimit = 20000,
            limitOverColor = limitsColor_0_50_Color,
            navigationLayoutType = NavigationLayoutType.NAVIGATION_RAIL,
        )
    }
}

@Preview
@Composable
private fun AmountTextLayoutLargeFontPreview() {
    val density = LocalDensity.current
    CompositionLocalProvider(
        LocalDensity provides Density(density = density.density, fontScale = 2f)
    ) {
        AppTheme(darkTheme = false) {
            AmountTextLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                totalExpense = 7639,
                expenseLimit = 20000,
                limitOverColor = limitsColor_0_50_Color,
            )
        }
    }
}
