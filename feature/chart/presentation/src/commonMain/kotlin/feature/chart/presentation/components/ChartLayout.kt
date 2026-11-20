@file:OptIn(ExperimentalResourceApi::class)

package feature.chart.presentation.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import feature.chart.presentation.ChartState
import ir.ehsannarmani.compose_charts.PieChart
import ir.ehsannarmani.compose_charts.models.Pie
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.total
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import toMoneyString

@ExperimentalFoundationApi
@Composable
fun ChartLayout(
    modifier: Modifier = Modifier,
    state: ChartState
) {
    var dynamicTextSize by remember {
        mutableStateOf(24.sp)
    }
    var pieData by remember(state.items) {
        mutableStateOf(
            state.items.mapNotNull { chart ->
                val typeSumCost = chart.items.sumOf { it.cost }
                if (typeSumCost <= 0L) {
                    null
                } else {
                    val color = Color(chart.type.colorArgb)
                    Pie(
                        label = chart.type.name,
                        data = typeSumCost.toDouble(),
                        color = color,
                        selectedColor = color
                    )
                }
            }
        )
    }
    val selectedPie = pieData.firstOrNull { it.selected }

    if (state.totalExpense != 0L && pieData.isNotEmpty()) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .sizeIn(
                            minWidth = 180.dp,
                            minHeight = 180.dp,
                            maxWidth = 240.dp,
                            maxHeight = 240.dp
                        )
                        .aspectRatio(1f),
                    contentAlignment = Alignment.Center
                ) {
                    PieChart(
                        modifier = Modifier.fillMaxSize(),
                        data = pieData,
                        onPieClick = { clickedPie ->
                            val pieIndex = pieData.indexOf(clickedPie)
                            pieData = pieData.mapIndexed { index, pie ->
                                pie.copy(selected = index == pieIndex && !pie.selected)
                            }
                        },
                        selectedScale = 1.1f,
                        scaleAnimEnterSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        ),
                        colorAnimEnterSpec = tween(300),
                        colorAnimExitSpec = tween(300),
                        scaleAnimExitSpec = tween(300),
                        spaceDegreeAnimEnterSpec = tween(300),
                        spaceDegreeAnimExitSpec = tween(300),
                        spaceDegree = 2f,
                        selectedPaddingDegree = 4f,
                        style = Pie.Style.Stroke(width = 28.dp)
                    )
                    Column(
                        modifier = Modifier.padding(horizontal = 36.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            modifier = Modifier.padding(vertical = 4.dp),
                            text = selectedPie?.label ?: stringResource(Res.string.total),
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = (selectedPie?.data?.toLong() ?: state.totalExpense).toMoneyString(),
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontSize = dynamicTextSize
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            onTextLayout = {
                                if (it.size.width > 300.dp.value.toInt() && dynamicTextSize > 9.sp) {
                                    dynamicTextSize = (dynamicTextSize.value - 1.0F).sp
                                }
                            }
                        )
                    }
                }
            }
        }
    }

}

