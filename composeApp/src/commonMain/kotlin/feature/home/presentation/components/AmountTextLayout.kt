
package feature.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import feature.core.presentation.navigation.NavigationLayoutType
import feature.core.ui.CorrectColor
import feature.core.ui.ErrorColor
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.expense
import moneymanagerkmp.composeapp.generated.resources.income
import moneymanagerkmp.composeapp.generated.resources.total
import org.jetbrains.compose.resources.stringResource
import toMoneyString

@Composable
fun AmountTextLayout(
    modifier: Modifier = Modifier,
    income: Long,
    expense: Long,
    total: Long,
    navigationLayoutType: NavigationLayoutType = NavigationLayoutType.BOTTOM_NAVIGATION
) {
    when(navigationLayoutType){
        NavigationLayoutType.BOTTOM_NAVIGATION -> {
            Box(
                modifier = modifier.background(Color.White),
                contentAlignment = Alignment.Center
            ){
                AmountTextLayoutNaviBottom(income, expense, total)
            }
        }
        else -> {
            Box(
                modifier = modifier
            ){
                AmountTextLayoutNaviRail(income, expense, total)
            }
        }
    }

}
@Composable
private fun AmountTextLayoutNaviRail(
    income: Long,
    expense: Long,
    total: Long,
){
    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AmountSection(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            income = income,
            expense =  expense,
            total =  total
        )
    }
}

@Composable
private fun AmountTextLayoutNaviBottom(
    income: Long,
    expense: Long,
    total: Long,
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
        ,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AmountSection(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 4.dp),
            income = income,
            expense =  expense,
            total =  total
        )

    }
}


@Composable
private fun AmountSection(
    modifier: Modifier = Modifier,
    total: Long,
    income: Long,
    expense: Long
) {

    AmountText(
        modifier = modifier,
        title = stringResource(Res.string.total),
        textColor = MaterialTheme.colorScheme.onBackground,
        text = total.toMoneyString(),
        textSize = 20.sp
    )
    AmountText(
        modifier = modifier,
        title = stringResource(Res.string.income),
        text = income.toMoneyString(),
        textColor = CorrectColor
    )
    AmountText(
        modifier =  modifier,
        title = stringResource(Res.string.expense),
        text = expense.toMoneyString(),
        textColor = ErrorColor
    )
}

@Composable
fun AmountText(
    modifier: Modifier = Modifier,
    title: String,
    text: String,
    textColor: Color,
    textSize: TextUnit = 20.sp
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
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge.copy(
                fontSize = textSize
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = textColor,
            onTextLayout = {
                if (it.hasVisualOverflow && dynamicTextSize > 9.sp){
                    dynamicTextSize = (dynamicTextSize.value - 1.0F).sp
                }
            },
            textAlign = TextAlign.Center
        )
    }
}