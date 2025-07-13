package feature.home.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import core.domain.model.Expense
import core.domain.model.Type
import core.presentation.CategoryList
import core.presentation.components.CircleIcon
import core.presentation.date.toDayOfWeekStringResource
import core.presentation.date.toLocalDateTime
import core.presentation.noRippleClick
import core.ui.bgColor
import core.ui.bglightColor
import core.ui.borderColor
import core.ui.borderMutedColor
import core.ui.highlightColor
import core.ui.textColor
import core.ui.textMutedColor
import feature.home.presentation.model.ExpenseUi
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.baseline_sticky_note_24
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import toDateString
import toMoneyString

@Composable
fun ExpenseItem(
    modifier: Modifier = Modifier,
    items: List<ExpenseUi>,
    onItemClick: (ExpenseUi) -> Unit = {},
    isClick: Boolean = true
){
    if (items.isNotEmpty()){
        val total = remember(items){
            items.sumOf { it.cost }
        }
        val localDateTime  = remember(items){
            (items.firstOrNull()?.timestamp?:0L).toLocalDateTime()
        }
        val dayOfWeekString = stringResource(localDateTime.toDayOfWeekStringResource())
        val date = remember(localDateTime){
            val dateString = toDateString(localDateTime.monthNumber,localDateTime.dayOfMonth)
            "$dateString $dayOfWeekString"
        }
        var itemTotalValueWidth by remember {
            mutableStateOf(0)
        }
        OutlinedCard(
            modifier = modifier,
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.outlinedCardColors(
                containerColor = MaterialTheme.colorScheme.bglightColor()
            )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = date,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Text(
                        onTextLayout = {
                            itemTotalValueWidth = it.size.width
                        },
                        text = total.toMoneyString(),
                        style = MaterialTheme.typography.titleMedium.copy(
                            textAlign = TextAlign.End
                        )
                    )
                }
                Box(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    contentAlignment = Alignment.CenterEnd
                ){
                    HorizontalDivider(
                        modifier = Modifier.width( with(LocalDensity.current){ itemTotalValueWidth.toDp() }),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                items.forEach {expense ->
                    val backgroundColor = remember(expense.type){
                        if (expense.type == null) {
                            CategoryList.getColorByTypeId(expense.typeId)
                        }else{
                            Color(expense.type.colorArgb)
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp)
                            .noRippleClick {
                                onItemClick(expense)
                            }
                        ,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircleIcon(
                            modifier = Modifier.size(36.dp),
                            isClicked = isClick,
                            image = CategoryList.getCategoryIconById(expense.categoryId.toLong()),
                            backgroundColor = backgroundColor
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Text(
                                text = expense.description,
                                style = MaterialTheme.typography.labelMedium
                            )
                            if (expense.content.trim().isNotEmpty()){
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    modifier = Modifier.size(12.dp),
                                    imageVector = vectorResource(Res.drawable.baseline_sticky_note_24),
                                    contentDescription = null
                                )
                            }
                        }
                        Text(
                            text = expense.cost.toMoneyString(),
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }
        }

    }
}