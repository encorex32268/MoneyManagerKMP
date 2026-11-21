@file:OptIn(ExperimentalResourceApi::class)

package feature.chart.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import core.domain.model.chart.Chart
import feature.chart.presentation.ChartState
import format
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.total
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import toMoneyString
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.hypot

private data class DonutSlice(
    val chart: Chart,
    val amount: Long,
    val color: Color,
    val startAngle: Float,
    val sweepAngle: Float,
    val percentage: Double
)

@ExperimentalFoundationApi
@Composable
fun ChartLayout(
    modifier: Modifier = Modifier,
    state: ChartState
) {
    val slices = remember(state.items, state.totalExpense) {
        val validItems = state.items.mapNotNull { chart ->
            val sum = chart.items.sumOf { it.cost }
            if (sum > 0L) chart to sum else null
        }
        val total = validItems.sumOf { it.second }.toFloat()
        if (total <= 0f) {
            emptyList()
        } else {
            var currentStart = -90f
            validItems.map { (chart, amount) ->
                val sweep = 360f * amount / total
                val slice = DonutSlice(
                    chart = chart,
                    amount = amount,
                    color = Color(chart.type.colorArgb),
                    startAngle = currentStart,
                    sweepAngle = sweep,
                    percentage = (amount * 100.0) / total
                )
                currentStart += sweep
                slice
            }
        }
    }

    var selectedIndex by remember(state.items) { mutableStateOf<Int?>(null) }
    val selectedSlice = selectedIndex?.let { slices.getOrNull(it) }

    val displayedTitle = if (selectedSlice != null) {
        "${selectedSlice.chart.type.name} (${selectedSlice.percentage.format(1)}%)"
    } else {
        stringResource(Res.string.total)
    }
    val displayedAmount = (selectedSlice?.amount ?: state.totalExpense).toMoneyString()

    var dynamicTextSize by remember(displayedAmount) {
        mutableStateOf(24.sp)
    }

    val chartProgress = remember(state.items) { Animatable(0f) }
    LaunchedEffect(state.items) {
        chartProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 800,
                easing = LinearOutSlowInEasing
            )
        )
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        if (state.totalExpense != 0L && slices.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .padding(12.dp)
                    .sizeIn(
                        minWidth = 180.dp,
                        minHeight = 180.dp,
                        maxWidth = 260.dp,
                        maxHeight = 260.dp
                    )
                    .aspectRatio(1f),
                contentAlignment = Alignment.Center
            ) {
                val selectedScaleAnimations = slices.indices.map { index ->
                    animateFloatAsState(
                        targetValue = if (selectedIndex == index) 1f else 0f,
                        animationSpec = tween(durationMillis = 250)
                    ).value
                }

                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(slices) {
                            detectTapGestures { offset ->
                                val center = Offset(size.width / 2f, size.height / 2f)
                                val dx = offset.x - center.x
                                val dy = offset.y - center.y
                                val distance = hypot(dx, dy)

                                val baseStrokePx = 30.dp.toPx()
                                val maxRadius = minOf(size.width, size.height) / 2f
                                val outerRadius = maxRadius
                                val innerRadius = (maxRadius - baseStrokePx).coerceAtLeast(0f)
                                val touchSlop = 12.dp.toPx()

                                if (distance < innerRadius - touchSlop) {
                                    selectedIndex = null
                                    return@detectTapGestures
                                }
                                if (distance > outerRadius + touchSlop) {
                                    return@detectTapGestures
                                }

                                val rawDeg = (atan2(dy, dx) * 180f / PI.toFloat())
                                val normalizedFromTop = (rawDeg + 90f + 360f) % 360f

                                val hitIndex = slices.indexOfFirst { slice ->
                                    val startFromTop = (slice.startAngle + 90f + 360f) % 360f
                                    val endFromTop = startFromTop + slice.sweepAngle
                                    normalizedFromTop in startFromTop..endFromTop
                                }

                                if (hitIndex != -1) {
                                    selectedIndex = if (selectedIndex == hitIndex) null else hitIndex
                                }
                            }
                        }
                ) {
                    val baseStrokePx = 30.dp.toPx()
                    val selectedExtraPx = 8.dp.toPx()
                    val maxStrokePx = baseStrokePx + selectedExtraPx
                    val diameter = minOf(size.width, size.height) - maxStrokePx
                    val topLeftOffset = Offset(
                        x = (size.width - diameter) / 2f,
                        y = (size.height - diameter) / 2f
                    )
                    val arcSize = Size(diameter, diameter)
                    val gapDegree = if (slices.size > 1) 2f else 0f

                    slices.forEachIndexed { index, slice ->
                        val selectionFraction = selectedScaleAnimations[index]
                        val strokeWidth = baseStrokePx + selectedExtraPx * selectionFraction
                        val sweep = (slice.sweepAngle - gapDegree).coerceAtLeast(0f) * chartProgress.value

                        drawArc(
                            color = slice.color,
                            startAngle = slice.startAngle + gapDegree / 2f,
                            sweepAngle = sweep,
                            useCenter = false,
                            topLeft = topLeftOffset,
                            size = arcSize,
                            style = Stroke(
                                width = strokeWidth,
                                cap = StrokeCap.Butt
                            )
                        )
                    }
                }

                Column(
                    modifier = Modifier.padding(horizontal = 40.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 2.dp),
                        text = displayedTitle,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = displayedAmount,
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontSize = dynamicTextSize
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        onTextLayout = {
                            if (it.hasVisualOverflow && dynamicTextSize > 9.sp) {
                                dynamicTextSize = (dynamicTextSize.value - 1.0F).sp
                            }
                        }
                    )
                }
            }
        }
    }
}
