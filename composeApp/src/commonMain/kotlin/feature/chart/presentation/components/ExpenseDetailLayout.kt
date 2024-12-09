package feature.chart.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import feature.chart.presentation.ChartState
import feature.core.domain.model.chart.Chart
import feature.core.presentation.CategoryList
import feature.core.presentation.Texts
import feature.core.presentation.components.CircleIcon
import feature.core.presentation.noRippleClick
import format
import org.jetbrains.compose.resources.ExperimentalResourceApi
import toMoneyString

@ExperimentalResourceApi
@Composable
fun ExpenseDetailLayout(
    modifier: Modifier = Modifier,
    items: List<Chart> = emptyList(),
    isIncomeShown: Boolean = false,
    sumTotal: Long,
    onItemClick: (Chart) -> Unit
) {
    if (sumTotal != 0L){
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items.forEach { chart ->
                val sum =  if (isIncomeShown) chart.itemsIncome.sumOf { it.cost } else chart.itemsNotIncome.sumOf { it.cost }
                if (sum != 0L){
                    ExpenseDetailItem(
                        onItemClick = onItemClick,
                        chart = chart,
                        percent = sum / sumTotal.toFloat(),
                        sum = sum
                    )

                }
            }
        }

    }
}

@Composable
fun ExpenseDetailItem(
    onItemClick: (Chart) -> Unit = {},
    chart: Chart,
    percent: Float,
    sum: Long
) {

    Row(
        modifier = Modifier
            .height(IntrinsicSize.Max)
            .padding(
                start = 24.dp,
                end = 8.dp
            )
            .noRippleClick {
                onItemClick(chart)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(24.dp).background(
                color = Color(chart.type.colorArgb),
                shape = RoundedCornerShape(8.dp)
            )
        )
        Spacer(Modifier.width(8.dp))
        Text(
            modifier = Modifier.widthIn(40.dp, 100.dp),
            text = chart.type.name,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(start = 4.dp),
            text = "${(percent * 100).toDouble().format(1)}%",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            modifier = Modifier
                .padding(horizontal = 25.dp),
            text = sum.toMoneyString(),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
