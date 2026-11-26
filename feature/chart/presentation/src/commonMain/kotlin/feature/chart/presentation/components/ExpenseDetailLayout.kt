package feature.chart.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import core.domain.model.chart.Chart
import core.ui.MoneyTheme
import format
import org.jetbrains.compose.resources.ExperimentalResourceApi
import toMoneyString

/**
 * D3 Category Breakdown List inside MoneyCard.
 */
@ExperimentalResourceApi
@Composable
fun ExpenseDetailLazyGrid(
    modifier: Modifier = Modifier,
    items: List<Chart> = emptyList(),
    sumTotal: Long,
    onItemClick: (Chart) -> Unit,
) {
    val nonEmptyItems = remember(items) {
        items.mapNotNull { chart ->
            val sum = chart.items.sumOf { it.cost }
            if (sum > 0L) chart to sum else null
        }.sortedByDescending { it.second }
    }

    if (sumTotal != 0L && nonEmptyItems.isNotEmpty()) {
        Column(modifier = modifier.fillMaxWidth()) {
            nonEmptyItems.forEachIndexed { index, (chart, sum) ->
                ExpenseDetailItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { onItemClick(chart) },
                    chart = chart,
                    percent = sum / sumTotal.toFloat(),
                    sum = sum,
                )
                if (index < nonEmptyItems.lastIndex) {
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = MoneyTheme.colors.line,
                    )
                }
            }
        }
    }
}

@Composable
fun ExpenseDetailItem(
    modifier: Modifier = Modifier,
    chart: Chart,
    percent: Float,
    sum: Long,
) {
    Row(
        modifier = modifier.padding(vertical = 12.dp, horizontal = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(
                        color = Color(chart.type.colorArgb),
                        shape = CircleShape,
                    )
            )
            Text(
                text = chart.type.name,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MoneyTheme.colors.ink,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = sum.toMoneyString(),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = MoneyTheme.colors.ink,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "${(percent * 100).toDouble().format(1)}%",
                style = MaterialTheme.typography.bodySmall,
                color = MoneyTheme.colors.muted,
            )
            Icon(
                modifier = Modifier.size(18.dp),
                imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = MoneyTheme.colors.muted,
            )
        }
    }
}
