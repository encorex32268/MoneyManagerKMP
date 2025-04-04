package feature.home.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import core.domain.model.Expense
import core.domain.model.Type
import core.presentation.CategoryList
import core.presentation.components.CircleIcon
import core.presentation.date.toDayOfWeekStringResource
import core.presentation.date.toLocalDateTime
import core.presentation.noRippleClick
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.baseline_sticky_note_24
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import toMoneyString

@Composable
fun ExpenseItem(
    modifier: Modifier = Modifier,
    items: List<Expense>,
    types: List<Type> = emptyList(),
    onItemClick: (Expense) -> Unit = {},
    isClick: Boolean = true
){
    if (items.isNotEmpty()){
        val total = remember(items){
            val expenseItems = items.filterNot { it.isIncome }
            expenseItems.sumOf { it.cost }
        }
        val localDateTime  = remember(items){
            (items.firstOrNull()?.timestamp?:0L).toLocalDateTime()
        }
        val dayOfWeekString = stringResource(localDateTime.toDayOfWeekStringResource())
        val date = remember(localDateTime){
            "${localDateTime.monthNumber}/${localDateTime.dayOfMonth} $dayOfWeekString"
        }
        OutlinedCard(
            modifier = modifier,
            shape = RoundedCornerShape(16.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = date,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                    Text(
                        text = total.toMoneyString(),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                items.forEach {expense ->
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
                        val findType = types.find {
                            it.typeIdTimestamp == expense.typeId
                        }
                        CircleIcon(
                            modifier = Modifier.size(36.dp),
                            isClicked = isClick,
                            image = CategoryList.getCategoryIconById(expense.categoryId.toLong()),
                            backgroundColor = if (findType == null) CategoryList.getColorByTypeId(expense.typeId) else Color(findType.colorArgb),
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