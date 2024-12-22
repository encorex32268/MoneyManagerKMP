@file:OptIn(ExperimentalResourceApi::class)

package feature.chart.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import feature.chart.presentation.ChartState
import feature.core.domain.model.Expense
import feature.core.domain.model.chart.Chart
import feature.core.presentation.CategoryList
import feature.core.presentation.Texts
import feature.home.presentation.components.AmountText
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
    val chartProgress = remember(state) {
        Animatable(0f)
    }
    var sweepAngle = 0f
    var startAngle = 0f

    LaunchedEffect(key1 = state) {
        chartProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1500,
                easing = LinearOutSlowInEasing
            )
        )

    }
    if (state.totalExpense != 0L){
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .align(Alignment.CenterHorizontally)
                ,
            ) {
                Row(
                    modifier = Modifier
                        .wrapContentHeight()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(3f),
                        contentAlignment = Alignment.Center
                    ){
                        Box(modifier = Modifier
                            .padding(16.dp)
                            .sizeIn(
                                minWidth = 150.dp,
                                minHeight = 150.dp,
                                maxWidth = 200.dp,
                                maxHeight = 200.dp
                            )
                            .drawBehind {
                                val totalExpense = state.totalExpense
                                state.items.forEachIndexed { index, item ->
                                    val typeSumCost = item.items.sumOf { it.cost }
                                    sweepAngle = 360f * typeSumCost / totalExpense
                                    if (index == 0) {
                                        startAngle = -90f
                                    } else {
                                        val chart = state.items[index - 1]
                                        val countList = chart.items
                                        startAngle += 360f * countList.sumOf { it.cost } / totalExpense
                                    }
                                    drawArc(
                                        color = Color(item.type.colorArgb),
                                        startAngle = startAngle * chartProgress.value,
                                        sweepAngle = sweepAngle * chartProgress.value,
                                        useCenter = false,
                                        style = Stroke(
                                            width = 30.dp.toPx()
                                        )
                                    )
                                }
                            },
                            contentAlignment = Alignment.Center
                        ) {
                            AmountText(
                                modifier = Modifier
                                    .padding(vertical = 4.dp),
                                title = stringResource(Res.string.total),
                                textColor = MaterialTheme.colorScheme.onBackground,
                                text = state.totalExpense.toMoneyString()
                            )
                        }
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        state.items.forEach { chart ->
                            val checkTotal =  remember(chart){
                                chart.items.sumOf{ it.cost }
                            }
                            if (checkTotal != 0L){
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ){
                                    Box(
                                        modifier = Modifier
                                            .size(12.dp)
                                            .clip(CircleShape)
                                            .background(
                                                color = Color(chart.type.colorArgb),
                                                shape = CircleShape
                                            )
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        text = chart.type.name,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }

                            }
                        }
                    }

                }
            }
        }
    }

}

