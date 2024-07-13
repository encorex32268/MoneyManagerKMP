package feature.presentation.add.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import feature.core.presentation.CategoryList
import feature.core.presentation.Texts
import feature.core.presentation.components.CircleIcon
import feature.core.presentation.model.CategoryUi
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.description
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ExpenseInfo(
    modifier: Modifier = Modifier,
    categoryUi: CategoryUi?,
    description: String,
    cost: Long,
    onValueChange: (String) -> Unit,
){
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CircleIcon(
            modifier = Modifier.size(48.dp),
            backgroundColor = CategoryList.getColorByCategory(categoryUi?.typeId?:0),
            image = CategoryList.getCategoryIconById(categoryUi?.categoryId?:0),
            isClicked = true,
        )
        Spacer(modifier = Modifier.width(8.dp))
        BasicTextField(
            modifier = Modifier.weight(1f),
            value = description,
            onValueChange = onValueChange,
            decorationBox = {
                if (description.isEmpty()){
                    Texts.BodyMedium(
                        text = stringResource(Res.string.description)
                    )
                }
                it()
            },
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Texts.TitleMedium(
            text = cost.toString(),
            maxLines = 1
        )
    }
}