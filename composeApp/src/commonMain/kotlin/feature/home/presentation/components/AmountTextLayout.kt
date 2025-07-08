
package feature.home.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.presentation.LocalDarkLightMode
import core.presentation.navigation.NavigationLayoutType
import core.ui.limitsColor_100_Color
import core.ui.limitsColor_50_70_Color
import core.ui.textColor
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.home_spending_limit
import moneymanagerkmp.composeapp.generated.resources.total_expense
import org.jetbrains.compose.resources.stringResource
import toMoneyString

@Composable
fun AmountTextLayout(
    modifier: Modifier = Modifier,
    totalExpense: Long,
    expenseLimit: Long,
    limitOverColor: Color,
    navigationLayoutType: NavigationLayoutType = NavigationLayoutType.BOTTOM_NAVIGATION,
    onExpenseLimitClick: () -> Unit = {}
) {
    when(navigationLayoutType){
        NavigationLayoutType.BOTTOM_NAVIGATION -> {
            Box(
                contentAlignment = Alignment.Center
            ){
                AmountTextLayoutNaviBottom(
                    totalExpense = totalExpense,
                    expenseLimit = expenseLimit,
                    onExpenseLimitClick = onExpenseLimitClick,
                    limitOverColor = limitOverColor
                )
            }
        }
        else -> {
            Box(
                modifier = modifier
            ){
                AmountTextLayoutNaviRail(
                    totalExpense = totalExpense,
                    expenseLimit = expenseLimit,
                    onExpenseLimitClick = onExpenseLimitClick,
                    limitOverColor = limitOverColor
                )
            }
        }
    }

}
@Composable
private fun AmountTextLayoutNaviRail(
    totalExpense: Long,
    expenseLimit: Long,
    limitOverColor: Color,
    onExpenseLimitClick: () -> Unit = {}
){
    val onBackgroundColor = MaterialTheme.colorScheme.onBackground

    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        AmountText(
            title = stringResource(Res.string.total_expense),
            text = totalExpense.toMoneyString(),
            textStyle = MaterialTheme.typography.labelLarge.copy(
                color = if (totalExpense > expenseLimit && expenseLimit != 0L) limitsColor_100_Color else onBackgroundColor,
                fontSize = 24.sp
            ),
        )
        Row(
            modifier = Modifier.clickable {
                onExpenseLimitClick()
            },
            verticalAlignment = Alignment.CenterVertically
        ){
            AmountText(
                title = stringResource(Res.string.home_spending_limit),
                text = expenseLimit.toMoneyString(),
            )
            Icon(
                modifier = Modifier
                    .size(12.dp)
                    .align(Alignment.CenterVertically),
                imageVector = Icons.Default.Edit,
                contentDescription = "SpendingLimit Edit"
            )

        }
    }
}

@Composable
private fun AmountTextLayoutNaviBottom(
    totalExpense: Long,
    expenseLimit: Long,
    limitOverColor: Color,
    onExpenseLimitClick: () -> Unit = {}

){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
        ,
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AmountSection(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 4.dp),
            totalExpense =  totalExpense,
            expenseLimit =  expenseLimit,
            onExpenseLimitClick = onExpenseLimitClick,
            limitOverColor = limitOverColor
        )

    }
}


@Composable
private fun AmountSection(
    modifier: Modifier = Modifier,
    totalExpense: Long,
    expenseLimit: Long,
    limitOverColor: Color,
    onExpenseLimitClick: () -> Unit = {}
) {

    val startColor = MaterialTheme.colorScheme.textColor()
    val totalTextColor = remember(expenseLimit){
        if (expenseLimit != 0L) limitOverColor else startColor
    }
    Row(
        modifier = modifier.clickable {
            onExpenseLimitClick()
        },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ){
        AmountText(
            title = stringResource(Res.string.total_expense),
            text = totalExpense.toMoneyString(),
            textStyle = MaterialTheme.typography.labelLarge.copy(
                fontSize = 24.sp,
                color = totalTextColor
            )
        )
        AmountText(
            title = "",
            text = "|",
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            AmountText(
                title = stringResource(Res.string.home_spending_limit),
                text = expenseLimit.toMoneyString(),
            )
            Icon(
                modifier = Modifier
                    .size(12.dp)
                    .align(Alignment.CenterVertically),
                imageVector = Icons.Default.Edit,
                contentDescription = "SpendingLimit Edit"
            )

        }
    }
}



@Composable
fun AmountText(
    modifier: Modifier = Modifier,
    title: String,
    text: String,
    textSize: TextUnit = 24.sp,
    textStyle: TextStyle = MaterialTheme.typography.labelLarge.copy(
        fontSize = textSize
    )
){
    var dynamicTextSize by remember {
        mutableStateOf(textSize)
    }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall
        )
        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = text,
            style = textStyle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = {
                if (it.hasVisualOverflow && dynamicTextSize > 9.sp){
                    dynamicTextSize = (dynamicTextSize.value - 1.0F).sp
                }
            },
            textAlign = TextAlign.Center
        )
    }
}