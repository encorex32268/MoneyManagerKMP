package feature.chart.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import feature.core.domain.model.chart.Chart
import format
import org.jetbrains.compose.resources.ExperimentalResourceApi
import toMoneyString

@ExperimentalResourceApi
@Composable
fun ExpenseDetailLazyGrid(
    modifier: Modifier = Modifier,
    items: List<Chart> = emptyList(),
    isIncomeShown: Boolean = false,
    sumTotal: Long,
    onItemClick: (Chart) -> Unit
) {
    if (sumTotal != 0L){

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ){
            items(items){ chart ->
                val sum =  if (isIncomeShown) chart.itemsIncome.sumOf { it.cost } else chart.itemsNotIncome.sumOf { it.cost }
                if (sum != 0L){
                    ExpenseDetailItem(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                onItemClick(chart)
                        },
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
    modifier: Modifier = Modifier,
    chart: Chart,
    percent: Float,
    sum: Long
) {
    Box(
        modifier = modifier
    ){
        Column(
            modifier = Modifier.padding(8.dp)
        ){
            Text(
                text = chart.type.name,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp
                )

            )
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ){
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            color = Color(chart.type.colorArgb),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clip(RoundedCornerShape(12.dp))
                )
                Text(
                    modifier = Modifier.weight(1f),
                    text = "${(percent * 100).toDouble().format(1)}%",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontSize = 24.sp
                    ),
                    maxLines = 1,
                    textAlign = TextAlign.End
                )
            }
            Spacer(Modifier.height(4.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 4.dp),
                text = sum.toMoneyString(),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.End
            )

        }

    }
}



