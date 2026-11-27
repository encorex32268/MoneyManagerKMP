package feature.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import app.presentation.AppTheme
import core.presentation.components.BudgetProgressRing
import core.presentation.components.BudgetRingHeroCard
import core.presentation.components.MoneyCard
import core.presentation.navigation.NavigationLayoutType
import core.ui.MoneyTheme
import core.ui.MoneyTokens
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.home_no_budget_limit
import moneymanagerkmp.composeapp.generated.resources.home_remaining_budget
import moneymanagerkmp.composeapp.generated.resources.total_expense
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.tooling.preview.Preview
import toMoneyString
import kotlin.math.roundToInt

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
            BudgetRingHeroCard(
                modifier = modifier,
                totalExpense = totalExpense,
                expenseLimit = expenseLimit,
                ringColor = if (expenseLimit > 0L) limitOverColor else null,
                onClick = onExpenseLimitClick,
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
private fun AmountSummaryCardRail(
    totalExpense: Long,
    expenseLimit: Long,
    limitOverColor: Color,
    onExpenseLimitClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val hasLimit = expenseLimit > 0L
    val rawRatio = if (hasLimit) (totalExpense / expenseLimit.toFloat()).coerceAtLeast(0f) else 0f
    val percentInt = (rawRatio * 100f).roundToInt().coerceAtMost(999)
    val remaining = (expenseLimit - totalExpense).coerceAtLeast(0L)
    val ringColor = if (hasLimit) limitOverColor else MoneyTheme.colors.muted

    MoneyCard(
        modifier = modifier.padding(8.dp),
        onClick = onExpenseLimitClick,
        contentPadding = PaddingValues(18.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            BudgetProgressRing(
                progress = if (hasLimit) rawRatio.coerceIn(0f, 1f) else 0f,
                percentText = if (hasLimit) "${percentInt}%" else "—",
                ringColor = ringColor,
                trackColor = MoneyTheme.colors.surfaceAlt,
            )
            Text(
                text = stringResource(Res.string.total_expense),
                style = MaterialTheme.typography.labelMedium,
                color = MoneyTheme.colors.muted,
            )
            Text(
                text = totalExpense.toMoneyString(),
                style = MaterialTheme.typography.headlineSmall,
                color = MoneyTheme.colors.ink,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
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
            limitOverColor = MoneyTokens.BudgetSafe,
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
            totalExpense = 14800,
            expenseLimit = 20000,
            limitOverColor = MoneyTokens.BudgetDanger,
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
            limitOverColor = MoneyTokens.BudgetSafe,
            navigationLayoutType = NavigationLayoutType.NAVIGATION_RAIL,
        )
    }
}
