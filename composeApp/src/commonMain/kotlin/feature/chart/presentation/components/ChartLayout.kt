@file:OptIn(ExperimentalResourceApi::class)

package feature.chart.presentation.components

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
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
    state: ChartState,
    sumTotal: Long = 0L
) {
    var isStart by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = Unit) {
        isStart = true
    }
    var sweepAngle = 0f
    var startAngle = 0f
    val animation by animateFloatAsState(
        targetValue = if (isStart) 1f else 0f, label = "",
        animationSpec = tween(1000)
    )
    if (sumTotal != 0L){
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                colors = CardDefaults.outlinedCardColors(
                    containerColor = Color.White
                )
            ) {
                Row(
                    modifier = Modifier
                        .wrapContentHeight()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier.weight(3f),
                        contentAlignment = Alignment.Center
                    ){
                        Box(modifier = Modifier
                            .padding(24.dp)
                            .sizeIn(
                                minWidth = 150.dp,
                                minHeight = 150.dp,
                                maxWidth = 200.dp,
                                maxHeight = 200.dp
                            )
                            .drawBehind {
                                state.items.forEachIndexed { index, item ->
                                    val typeSumCost = if (state.isIncomeShown)
                                        item.itemsIncome.sumOf { it.cost }
                                    else {
                                        item.itemsNotIncome.sumOf { it.cost }
                                    }
                                    sweepAngle = 360f * typeSumCost / sumTotal
                                    if (index == 0) {
                                        startAngle = -90f
                                    } else {
                                        val chart = state.items[index - 1]
                                        val countList = if (state.isIncomeShown) chart.itemsIncome else chart.itemsNotIncome
                                        startAngle += 360f * countList.sumOf { it.cost } / sumTotal
                                    }
                                    drawArc(
                                        color = Color(item.type.colorArgb),
                                        startAngle = startAngle * animation,
                                        sweepAngle = sweepAngle * animation,
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
                                style = MaterialTheme.typography.titleMedium.copy(
                                    textAlign = TextAlign.Center
                                ),
                                textColor = MaterialTheme.colorScheme.onBackground,
                                text = sumTotal.toMoneyString(),
                                textSize = 20.sp
                            )
                        }
                    }
                    Spacer(Modifier.width(20.dp))
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val sort = state.items.sortedByDescending {
                            if (state.isIncomeShown){
                                it.itemsIncome.sumOf { it.cost }
                            }else{
                                it.itemsNotIncome.sumOf { it.cost }
                            }
                        }
                        sort.forEach { chart ->
                            val checkTotal = if (state.isIncomeShown) {
                                chart.itemsIncome.sumOf { it.cost }
                            }else{
                                chart.itemsNotIncome.sumOf { it.cost }
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
                                    Texts.BodySmall(
                                        text = chart.type.name
                                    )
                                }

                            }
                        }
                    }


                }

            }
            Spacer(modifier = Modifier.height(8.dp))



        }

    }
    if (sumTotal != 0L){

    }

}

