package feature.home.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import feature.core.domain.model.Expense
import feature.core.domain.model.Type
import feature.core.presentation.CategoryList
import feature.core.presentation.Texts
import feature.core.presentation.components.CircleIcon
import feature.core.presentation.date.DateConverter
import feature.core.presentation.date.toDayOfWeekStringResource
import feature.core.presentation.date.toLocalDateTime
import feature.core.presentation.noRippleClick
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.baseline_sticky_note_24
import moneymanagerkmp.composeapp.generated.resources.total_expense_item
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import toMoneyString
import kotlin.math.exp

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
            val incomeItems = items.filter { it.isIncome }
            val expenseItems = items.filterNot { it.isIncome }
            val income = incomeItems.sumOf { it.cost }
            val expense = expenseItems.sumOf { it.cost }
            -expense + income
        }
        val localDateTime  = remember(items){
            (items.firstOrNull()?.timestamp?:0L).toLocalDateTime()
        }
        val dayOfWeekString = stringResource(localDateTime.toDayOfWeekStringResource())
        val date = remember(localDateTime){
            "${localDateTime.monthNumber}/${localDateTime.dayOfMonth} $dayOfWeekString"
        }
        OutlinedCard(
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            modifier = modifier,
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(
                width = 0.dp ,
                color = MaterialTheme.colorScheme.outlineVariant
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
                            it.typeIdTimestamp == expense.typeId.toLong()
                        }
                        CircleIcon(
                            modifier = Modifier.size(36.dp),
                            isClicked = isClick,
                            image = CategoryList.getCategoryIconById(expense.categoryId.toLong()),
                            backgroundColor = if (findType == null) CategoryList.getColorByTypeId(expense.typeId) else Color(findType.colorArgb),
                            colorCheck = true
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Text(
                                text = expense.description,
                                style = MaterialTheme.typography.labelSmall
                            )
                            if (expense.content.trim().isNotEmpty()){
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    modifier = Modifier.size(12.dp),
                                    imageVector = vectorResource(Res.drawable.baseline_sticky_note_24),
                                    contentDescription = null,
                                    tint = Color.Gray
                                )
                            }
                        }
                        val expenseCost = remember(expense.isIncome){
                            if (expense.isIncome) expense.cost.toMoneyString() else "-${expense.cost.toMoneyString()}"
                        }
                        Text(
                            text = expenseCost,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }

    }
}