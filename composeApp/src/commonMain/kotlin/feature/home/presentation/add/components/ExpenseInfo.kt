package feature.home.presentation.add.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import core.presentation.CategoryList
import core.presentation.components.CircleIcon
import core.presentation.model.CategoryUi
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.description
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import toMoneyString

@Composable
fun ExpenseInfo(
    modifier: Modifier = Modifier,
    categoryUi: CategoryUi?,
    description: String,
    cost: Long,
    onValueChange: (String) -> Unit,
){
    Row(
        modifier = modifier.padding(end = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CircleIcon(
            modifier = Modifier.size(36.dp),
            backgroundColor = if (categoryUi?.colorArgb == null) CategoryList.getColorByTypeId(categoryUi?.typeId?:0) else Color(categoryUi.colorArgb),
            image = CategoryList.getCategoryIconById(categoryUi?.id?.toLong()?:0),
            isClicked = true
        )
        Spacer(modifier = Modifier.width(12.dp))
        BasicTextField(
            modifier = Modifier.weight(1f),
            value = description,
            onValueChange = onValueChange,
            decorationBox = {
                if (description.isEmpty()){
                    Text(
                        text = stringResource(Res.string.description),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 16.sp
                        )
                    )
                }
                it()
            },
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground
            ),
            cursorBrush = SolidColor(
                value = MaterialTheme.colorScheme.onBackground
            ),
            maxLines = 1
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = cost.toMoneyString(),
            maxLines = 1,
            style = MaterialTheme.typography.labelLarge.copy(
                fontSize = 20.sp
            )
        )
    }
}