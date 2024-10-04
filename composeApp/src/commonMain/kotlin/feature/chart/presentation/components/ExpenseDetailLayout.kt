package feature.chart.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
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
    state: ChartState,
    sumTotal: Long,
    onItemClick: (Chart) -> Unit
) {
    if (sumTotal != 0L){
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            state.items.forEach {
                val sum = if (state.isIncomeShown) it.itemsIncome.sumOf { it.cost } else it.itemsNotIncome.sumOf { it.cost }
                if (sum != 0L){
                    val percent = sum / sumTotal.toFloat()
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .noRippleClick {
                                onItemClick(it)
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Box(
                            modifier = Modifier.size(24.dp).background(
                                color = Color(it.type.colorArgb),
                                shape = RoundedCornerShape(8.dp)
                            )
                        )
                        Spacer(Modifier.width(8.dp))
                        Texts.TitleSmall(
                            modifier = Modifier.widthIn(40.dp , 100.dp),
                            text = it.type.name
                        )
                        Texts.TitleSmall(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(2f)
                                .padding(start = 4.dp),
                            text = "${(percent * 100).toDouble().format(2)}%",
                        )
                        Spacer(Modifier.weight(1f))
                        Texts.TitleMedium(
                            modifier = Modifier
                                .padding(horizontal = 25.dp),
                            text = sum.toMoneyString(),
                        )
                    }

                }
            }
        }

    }
}
