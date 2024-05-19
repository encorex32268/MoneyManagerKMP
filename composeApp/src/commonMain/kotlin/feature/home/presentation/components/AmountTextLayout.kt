@file:OptIn(ExperimentalResourceApi::class)

package feature.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import feature.core.presentation.Texts
import feature.core.ui.CorrectColor
import feature.core.ui.ErrorColor
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.expense
import moneymanagerkmp.composeapp.generated.resources.income
import moneymanagerkmp.composeapp.generated.resources.total
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import toMoneyString

@OptIn(ExperimentalResourceApi::class)
@Composable
fun AmountTextLayout(
    modifier: Modifier = Modifier,
    income: Long,
    expense: Long,
    total: Long
) {
    OutlinedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = Color.Transparent
        )
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
            ,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AmountText(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 4.dp),
                title = stringResource(Res.string.total),
                style = MaterialTheme.typography.titleMedium.copy(
                    textAlign = TextAlign.Center
                ),
                textColor = MaterialTheme.colorScheme.onBackground,
                text = total.toMoneyString(),
                textSize = 20.sp
            )
            AmountText(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 4.dp),
                title = stringResource(Res.string.income),
                text = income.toMoneyString(),
                textColor = CorrectColor,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontSize = 14.sp
                )
            )
            AmountText(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 4.dp),
                title = stringResource(Res.string.expense),
                text = expense.toMoneyString(),
                textColor = ErrorColor,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontSize = 14.sp
                )
            )
        }

    }
}

@Composable
private fun AmountText(
    modifier: Modifier = Modifier,
    title: String,
    text: String,
    textColor: Color,
    style: TextStyle,
    textSize: TextUnit = 14.sp
){
    var dynamicTextSize by remember {
        mutableStateOf(textSize)
    }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Texts.BodySmall(text = title)
        Spacer(modifier = Modifier.width(20.dp))
        Texts.TitleSmall(
            text = text,
            style = style.copy(
                fontSize = dynamicTextSize
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = textColor,
            onTextLayout = {
                if (it.hasVisualOverflow && dynamicTextSize > 9.sp){
                    dynamicTextSize = (dynamicTextSize.value - 1.0F).sp
                }
            }
        )
    }
}