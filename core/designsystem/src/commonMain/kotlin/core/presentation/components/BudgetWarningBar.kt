package core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import app.presentation.AppTheme
import core.ui.MoneyTheme
import core.ui.MoneyTokens
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.budget_zone_danger
import moneymanagerkmp.composeapp.generated.resources.budget_zone_safe
import moneymanagerkmp.composeapp.generated.resources.budget_zone_warn
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun BudgetWarningBar(
    modifier: Modifier = Modifier,
    warnMidPercent: Int = MoneyTokens.DefaultWarnMidPercent,
    warnHighPercent: Int = MoneyTokens.DefaultWarnHighPercent,
    showLabels: Boolean = true,
) {
    val safeWeight = warnMidPercent.coerceIn(5, 90).toFloat()
    val warnWeight = (warnHighPercent - warnMidPercent).coerceAtLeast(5).toFloat()
    val dangerWeight = (100 - warnHighPercent).coerceAtLeast(5).toFloat()

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (showLabels) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(Res.string.budget_zone_safe, warnMidPercent),
                    style = MaterialTheme.typography.labelSmall,
                    color = MoneyTheme.colors.budgetSafe,
                )
                Text(
                    text = stringResource(Res.string.budget_zone_warn, warnMidPercent, warnHighPercent),
                    style = MaterialTheme.typography.labelSmall,
                    color = MoneyTheme.colors.budgetWarn,
                )
                Text(
                    text = stringResource(Res.string.budget_zone_danger, warnHighPercent),
                    style = MaterialTheme.typography.labelSmall,
                    color = MoneyTheme.colors.budgetDanger,
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(999.dp))
                .background(MoneyTheme.colors.surfaceAlt),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Box(
                modifier = Modifier
                    .weight(safeWeight)
                    .height(8.dp)
                    .background(MoneyTheme.colors.budgetSafe)
            )
            Box(
                modifier = Modifier
                    .weight(warnWeight)
                    .height(8.dp)
                    .background(MoneyTheme.colors.budgetWarn)
            )
            Box(
                modifier = Modifier
                    .weight(dangerWeight)
                    .height(8.dp)
                    .background(MoneyTheme.colors.budgetDanger)
            )
        }
    }
}

@Preview
@Composable
private fun BudgetWarningBarLightPreview() {
    AppTheme(darkTheme = false) {
        BudgetWarningBar(modifier = Modifier.padding(16.dp))
    }
}

@Preview
@Composable
private fun BudgetWarningBarDarkPreview() {
    AppTheme(darkTheme = true) {
        BudgetWarningBar(modifier = Modifier.padding(16.dp))
    }
}
