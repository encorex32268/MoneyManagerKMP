@file:OptIn(ExperimentalResourceApi::class)

package feature.chart.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradient
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import feature.chart.domain.model.Chart
import feature.core.domain.model.Expense
import feature.core.presentation.CategoryList
import feature.core.presentation.Texts
import feature.core.presentation.components.CircleIcon
import feature.home.presentation.components.AmountText
import format
import kotlinx.coroutines.launch
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.expense
import moneymanagerkmp.composeapp.generated.resources.income
import moneymanagerkmp.composeapp.generated.resources.total
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import toMoneyString

@ExperimentalFoundationApi
@Composable
fun ChartLayout(
    modifier: Modifier = Modifier,
    items: List<Chart> = emptyList(),
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
                    Box(modifier = Modifier
                        .padding(24.dp)
                        .size(200.dp)
                        .drawBehind {
                            items.forEachIndexed { index, item ->
                                val typeSumCost = item.expenseItems.sumOf { it.cost }
                                sweepAngle = 360f * typeSumCost / sumTotal
                                if (index == 0) {
                                    startAngle = -90f
                                } else {
                                    startAngle += 360f * items[index - 1].expenseItems.sumOf { it.cost } / sumTotal
                                }
                                drawArc(
                                    color = CategoryList.getColorByCategory(item.typeId),
                                    startAngle = startAngle * animation,
                                    sweepAngle = sweepAngle * animation,
                                    useCenter = false,
                                    style = Stroke(
                                        width = 40.dp.toPx()
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
                    Spacer(Modifier.width(20.dp))
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val sort = items.sortedByDescending {
                            it.expenseItems.sumOf { it.cost }
                        }
                        sort.forEach {
                            Row {
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .clip(CircleShape)
                                        .background(
                                            color = CategoryList.getColorByCategory(it.typeId),
                                            shape = CircleShape
                                        )
                                )
                                Spacer(Modifier.width(8.dp))
                                Texts.BodySmall(
                                    text = CategoryList.getTypeStringByTypeId(it.typeId)
                                )
                            }
                        }
                    }


                }

            }
            Spacer(modifier = Modifier.height(8.dp))



        }

    }

}
