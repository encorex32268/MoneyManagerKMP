package feature.presentation.chart.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import feature.core.domain.model.chart.Chart
import feature.core.presentation.CategoryList
import feature.core.presentation.Texts
import feature.core.presentation.components.CircleIcon
import format
import org.jetbrains.compose.resources.ExperimentalResourceApi
import toMoneyString

@ExperimentalResourceApi
@Composable
fun ExpenseDetailLayout(
    modifier: Modifier = Modifier,
    items: List<Chart>,
    sumTotal: Long,
    onItemClick: (Chart) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items.forEach {
            val sum = it.expenseItems.sumOf { it.cost }
            val percent = sum / sumTotal.toFloat()
            Row(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable {
                        onItemClick(it)
                    },
                verticalAlignment = Alignment.CenterVertically
            ){
                CircleIcon(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .size(36.dp)
                    ,
                    backgroundColor =  CategoryList.getColorByCategory(it.typeId),
                    image = CategoryList.getTypeIconByTypeId(it.typeId),
                    isClicked = true,
                    onItemClick = {

                    },
                )
                Texts.TitleSmall(
                    modifier = Modifier.widthIn(40.dp , 100.dp),
                    text = CategoryList.getTypeStringByTypeId(it.typeId),
                )
                Texts.TitleSmall(
                    modifier = Modifier
                        .weight(2f)
                        .padding(start = 4.dp),
                    text = "${(percent * 100).toDouble().format(2)}%",
                )
                Spacer(Modifier.weight(1f))
                Texts.TitleMedium(
                    modifier = Modifier
                        .padding(horizontal = 25.dp),
                    text = sum.toMoneyString(),
                )
            }
        }
    }
}
