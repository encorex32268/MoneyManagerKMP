package feature.home.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import feature.core.domain.model.Expense
import feature.core.domain.model.Type
import feature.core.presentation.CategoryList
import feature.core.presentation.Texts
import feature.core.presentation.components.CircleIcon
import feature.core.presentation.date.DateConverter
import feature.core.presentation.noRippleClick
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.total_expense_item
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
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
        val total = mutableStateOf(
            run {
                val incomeItems = items.filter { it.isIncome }
                val expenseItems = items.filterNot { it.isIncome }
                val income = incomeItems.sumOf { it.cost }
                val expense = expenseItems.sumOf { it.cost }
                -expense + income
            }
        )
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
                    Texts.BodyMedium(
                        modifier = Modifier.weight(1f),
                        text = kotlin.run {
                            val localDateTime =DateConverter.getLocalDateTimeFromTimestamp(items[0].timestamp)
                            "${localDateTime.monthNumber}/${localDateTime.dayOfMonth} ${DateConverter.getDayOfWeekStringByDayOfWeek(localDateTime.dayOfWeek)}"
                        }
                    )
                    Texts.BodyMedium(
                        text = stringResource(
                            Res.string.total_expense_item,
                            total.value.toMoneyString()
                        )
                    )
                }
                items.forEach {expense ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
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
                            backgroundColor = if (findType == null) CategoryList.getColorByTypeId(expense.typeId.toLong()) else Color(findType.colorArgb)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Texts.BodySmall(
                            modifier = Modifier.weight(1f),
                            text = expense.description,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Texts.BodySmall(
                            text = if (expense.isIncome) expense.cost.toMoneyString() else "-${expense.cost.toMoneyString()}",
                        )
                    }
                }
            }
        }

    }
}