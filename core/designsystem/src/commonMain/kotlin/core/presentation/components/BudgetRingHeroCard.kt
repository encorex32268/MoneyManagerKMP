package core.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.presentation.AppTheme
import core.ui.MoneyTheme
import core.ui.MoneyTokens
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.home_monthly_expense_kicker
import moneymanagerkmp.composeapp.generated.resources.home_no_budget_limit
import moneymanagerkmp.composeapp.generated.resources.home_remaining_budget
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import toMoneyString
import kotlin.math.roundToInt

/**
 * D3 · Ring & Clarity Hero Card showing monthly total expense, remaining budget,
 * and animated circular budget progress ring.
 */
@Composable
fun BudgetRingHeroCard(
    totalExpense: Long,
    expenseLimit: Long,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    ringColor: Color? = null,
    warnMidPercent: Int = MoneyTokens.DefaultWarnMidPercent,
    warnHighPercent: Int = MoneyTokens.DefaultWarnHighPercent,
) {
    val hasLimit = expenseLimit > 0L
    val rawRatio = if (hasLimit) (totalExpense / expenseLimit.toFloat()).coerceAtLeast(0f) else 0f
    val clampedRatio = rawRatio.coerceIn(0f, 1f)
    val percentInt = (rawRatio * 100f).roundToInt().coerceAtMost(999)
    val remaining = (expenseLimit - totalExpense).coerceAtLeast(0L)

    val resolvedRingColor = ringColor ?: when {
        !hasLimit -> MoneyTheme.colors.muted
        percentInt < warnMidPercent -> MoneyTheme.colors.budgetSafe
        percentInt < warnHighPercent -> MoneyTheme.colors.budgetWarn
        else -> MoneyTheme.colors.budgetDanger
    }

    val animatedSweep = remember { Animatable(0f) }
    LaunchedEffect(clampedRatio) {
        animatedSweep.animateTo(
            targetValue = clampedRatio,
            animationSpec = tween(durationMillis = 700, easing = LinearOutSlowInEasing),
        )
    }

    MoneyCard(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        contentPadding = PaddingValues(20.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = stringResource(Res.string.home_monthly_expense_kicker),
                    style = MaterialTheme.typography.labelMedium,
                    color = MoneyTheme.colors.muted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = totalExpense.toMoneyString(),
                    style = MaterialTheme.typography.displaySmall,
                    color = MoneyTheme.colors.ink,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = if (hasLimit) {
                        stringResource(
                            Res.string.home_remaining_budget,
                            remaining.toMoneyString(),
                            expenseLimit.toMoneyString(),
                        )
                    } else {
                        stringResource(Res.string.home_no_budget_limit)
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MoneyTheme.colors.muted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            BudgetProgressRing(
                progress = if (hasLimit) animatedSweep.value else 0f,
                percentText = if (hasLimit) "${percentInt}%" else "—",
                ringColor = resolvedRingColor,
                trackColor = MoneyTheme.colors.surfaceAlt,
            )
        }
    }
}

@Composable
fun BudgetProgressRing(
    progress: Float,
    percentText: String,
    ringColor: Color,
    trackColor: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.size(78.dp),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 7.dp.toPx()
            val inset = strokeWidth / 2f
            val arcSize = Size(size.width - strokeWidth, size.height - strokeWidth)
            val topLeft = Offset(inset, inset)

            drawArc(
                color = trackColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
            )
            if (progress > 0f) {
                drawArc(
                    color = ringColor,
                    startAngle = -90f,
                    sweepAngle = 360f * progress.coerceIn(0f, 1f),
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                )
            }
        }
        Text(
            text = percentText,
            style = MaterialTheme.typography.labelLarge.copy(
                fontSize = 14.sp,
                fontWeight = FontWeight.ExtraBold,
            ),
            color = ringColor,
            maxLines = 1,
        )
    }
}

@Preview
@Composable
private fun BudgetRingHeroSafePreview() {
    AppTheme(darkTheme = false) {
        BudgetRingHeroCard(
            modifier = Modifier.padding(16.dp),
            totalExpense = 11460,
            expenseLimit = 30000,
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun BudgetRingHeroWarnPreview() {
    AppTheme(darkTheme = false) {
        BudgetRingHeroCard(
            modifier = Modifier.padding(16.dp),
            totalExpense = 18000,
            expenseLimit = 30000,
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun BudgetRingHeroDangerPreview() {
    AppTheme(darkTheme = false) {
        BudgetRingHeroCard(
            modifier = Modifier.padding(16.dp),
            totalExpense = 25500,
            expenseLimit = 30000,
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun BudgetRingHeroZeroLimitPreview() {
    AppTheme(darkTheme = false) {
        BudgetRingHeroCard(
            modifier = Modifier.padding(16.dp),
            totalExpense = 5100,
            expenseLimit = 0,
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun BudgetRingHeroDarkPreview() {
    AppTheme(darkTheme = true) {
        BudgetRingHeroCard(
            modifier = Modifier.padding(16.dp),
            totalExpense = 11460,
            expenseLimit = 30000,
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun BudgetRingHeroLargeFontPreview() {
    val density = LocalDensity.current
    CompositionLocalProvider(
        LocalDensity provides Density(density = density.density, fontScale = 2f)
    ) {
        AppTheme(darkTheme = false) {
            BudgetRingHeroCard(
                modifier = Modifier.padding(16.dp),
                totalExpense = 11460,
                expenseLimit = 30000,
                onClick = {},
            )
        }
    }
}
