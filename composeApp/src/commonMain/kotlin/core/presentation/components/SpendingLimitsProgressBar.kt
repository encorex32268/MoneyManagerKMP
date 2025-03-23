package core.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.presentation.LocalDarkLightMode
import core.ui.limitsColor_0_50_Color
import core.ui.limitsColor_50_70_Color
import core.ui.limitsColor_70_90_Color
import core.ui.limitsColor_0_50_Color
import core.ui.limitsColor_50_70_Color
import core.ui.limitsColor_70_90_Color
import format
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.home_spending_limit
import moneymanagerkmp.composeapp.generated.resources.total_expense
import org.jetbrains.compose.resources.stringResource
import toMoneyString

@Composable
fun SpendingLimitsProgressBar(
    modifier: Modifier = Modifier,
    spendingLimit: Long,
    totalExpense: Long,
    isShowLimitAndExpense: Boolean = true
) {
    val isDarkMode = LocalDarkLightMode.current
    val animatable = remember(isDarkMode) { Animatable(0f) }

    LaunchedEffect(spendingLimit, totalExpense) {
        animatable.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1500,
                easing = LinearOutSlowInEasing
            )
        )
    }

    val progress = if (spendingLimit > 0) {
        totalExpense / spendingLimit.toFloat()
    } else {
        0f
    }
    val animatedProgress = progress * animatable.value

    val errorColor = MaterialTheme.colorScheme.error

    val limitOverColor = remember(spendingLimit , totalExpense){
        when(progress){
            in 0.0f..0.5f -> limitsColor_0_50_Color
            in 0.5f..0.7f -> limitsColor_50_70_Color
            in 0.7f..0.9f -> limitsColor_70_90_Color
            else -> errorColor
        }
    }
    Column(
        modifier = modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ){
        if (isShowLimitAndExpense){
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ){
                Row(
                    modifier = Modifier.weight(1f)
                ){
                    Text(
                        text = stringResource(Res.string.total_expense),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = " / ",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = stringResource(Res.string.home_spending_limit),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Text(
                    text = totalExpense.toMoneyString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = limitOverColor
                )
                Text(
                    text = " / ",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = spendingLimit.toMoneyString(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){

            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .height(36.dp)
                    .padding(8.dp),
                progress = {
                    animatedProgress
                },
                strokeCap = StrokeCap.Round,
                color = limitOverColor,
                drawStopIndicator = {}
            )
            Text(
                text =  "${(progress * 100).toDouble().format(1)}%",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold
                ),
                color = limitOverColor
            )
        }


    }


}
