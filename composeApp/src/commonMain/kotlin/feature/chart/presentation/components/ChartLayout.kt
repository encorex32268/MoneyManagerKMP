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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import feature.chart.domain.model.Chart
import feature.core.domain.model.Expense
import feature.core.presentation.CategoryList
import feature.core.presentation.Texts
import feature.core.presentation.components.CircleIcon
import format
import kotlinx.coroutines.launch
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.expense
import moneymanagerkmp.composeapp.generated.resources.income
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import toMoneyString

@ExperimentalFoundationApi
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChartLayout(
    modifier: Modifier = Modifier,
    items: List<Chart> = emptyList(),
    isIncomeShow: Boolean = false,
    onChartClick: () -> Unit = {},
    detailExpense : List<Pair<Int,List<Expense>>> = emptyList(),
) {
    val costItems = remember(isIncomeShow) {
        if (isIncomeShow) {
            items.map {
                Pair(it.typeId, it.income)
            }
        } else {
            items.map {
                Pair(it.typeId, it.expense)
            }
        }
    }
    val sum = remember(isIncomeShow) {
        items.sumOf {
            if (isIncomeShow) {
                it.income
            } else {
                it.expense
            }
        }
    }
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

    val pagerState = rememberPagerState(pageCount = { 2 })
    val selectedTabIndex = remember { derivedStateOf { pagerState.currentPage } }
    val scope = rememberCoroutineScope()
    TabRow(
        selectedTabIndex = selectedTabIndex.value,
        modifier = Modifier.fillMaxWidth()
    ) {
        Tab(
            selected = selectedTabIndex.value == 1,
            onClick = {
                scope.launch {
                    pagerState.animateScrollToPage(1)
                    onChartClick()
                }
            },
            text = {
                Text(text = stringResource(Res.string.income))
            }
        )
        Tab(
            selected = selectedTabIndex.value == 2,
            onClick = {
                scope.launch {
                    pagerState.animateScrollToPage(2)
                    onChartClick()
                }
            },
            text = {
                Text(text = stringResource(Res.string.expense))
            }
        )
    }
    Spacer(Modifier.height(16.dp))
    HorizontalPager(state = pagerState) { page ->
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .align(Alignment.CenterHorizontally),
                colors = CardDefaults.outlinedCardColors(
                    containerColor = Color.Transparent
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.width(16.dp))
                    Box(modifier = Modifier
                        .size(150.dp)
                        .drawBehind {
                            if (sum != 0L) {
                                costItems.forEachIndexed { index, item ->
                                    sweepAngle = 360f * item.second / sum
                                    if (index == 0) {
                                        startAngle = -90f
                                    } else {
                                        startAngle += 360f * costItems[index - 1].second / sum
                                    }
                                    drawArc(
                                        color = CategoryList.getColorByCategory(item.first),
                                        startAngle = startAngle * animation,
                                        sweepAngle = sweepAngle * animation,
                                        useCenter = false,
                                        style = Stroke(
                                            width = 40.dp.toPx()
                                        )
                                    )
                                }

                            }
                        },
                        contentAlignment = Alignment.Center
                    ) {
                        //TODO : Show Total
                        Texts.TitleMedium(
                            text = if (isIncomeShow)
                                stringResource(Res.string.income) else
                                stringResource(Res.string.expense),
                            modifier = Modifier.padding(8.dp),
                        )
                    }
                    Spacer(modifier = Modifier.width(50.dp))
                    if (sum != 0L){
                        Column {
                            costItems.sortedByDescending { it.second }.forEach { item ->
                                if (item.second != 0L){
                                    val width: Float = (item.second / sum.toFloat())
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(12.dp)
                                                .clip(CircleShape)
                                                .background(
                                                    color = CategoryList.getColorByCategory(item.first),
                                                    shape = CircleShape
                                                )
                                        )
                                        Texts.BodySmall(
                                            text = CategoryList.getTypeStringByTypeId(item.first)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                }

                            }

                        }

                    }

                }

            }
            if (sum != 0L){
                Spacer(modifier = Modifier.height(8.dp))
                DetailExpense(
                    expensesTypeList = detailExpense
                )

            }
        }


    }



}

@ExperimentalMaterial3Api
@Composable
private fun DetailExpense(
    modifier: Modifier = Modifier,
    expensesTypeList : List<Pair<Int,List<Expense>>>
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        expensesTypeList.forEachIndexed { index,  pair->
            val sum = pair.second.sumOf { it.cost }
            Row(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable {

                    }
                ,
                verticalAlignment = Alignment.CenterVertically
            ){
                CircleIcon(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .padding(top = 8.dp)
                        .size(36.dp)
                    ,
                    backgroundColor =  CategoryList.getColorByCategory(pair.first),
                    image = CategoryList.getTypeIconByTypeId(pair.first),
                    isClicked = true,
                    id = pair.first,
                    onItemClick = {

                    },
                )
                Spacer(Modifier.weight(1f))
                Texts.TitleSmall(
                    modifier = Modifier
                        .padding(horizontal = 25.dp),
                    text = sum.toMoneyString(),
                )

            }

        }
    }

}
