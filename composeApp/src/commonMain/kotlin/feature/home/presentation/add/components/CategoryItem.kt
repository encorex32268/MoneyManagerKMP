@file:OptIn(ExperimentalResourceApi::class)

package feature.home.presentation.add.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import core.presentation.CategoryList
import core.presentation.components.CircleIcon
import core.presentation.model.CategoryUi
import core.presentation.noRippleClick
import org.jetbrains.compose.resources.ExperimentalResourceApi

@Composable
fun CategoryItem(
    modifier: Modifier = Modifier,
    categoryUi: CategoryUi,
    isClicked: Boolean,
    onItemClick: (() -> Unit)? = null,
    isVisibilityText: Boolean = true,
    isNeedDeleteIcon: Boolean = false,
    onDeleteClick: () -> Unit = {}
){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
        ){
            CircleIcon(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .padding(top = 8.dp)
                    .size(42.dp)
                ,
                backgroundColor = if (categoryUi.colorArgb == null) CategoryList.getColorByTypeId(categoryUi.typeId?:0) else Color(categoryUi.colorArgb),
                image = CategoryList.getCategoryIconById(categoryUi.id.toLong()),
                isClicked = isClicked,
                id = categoryUi.id,
                onItemClick = {
                    onItemClick?.let {
                        onItemClick()
                    }
                },
            )
            if (isNeedDeleteIcon){
                Icon(
                    modifier = Modifier
                        .size(12.dp)
                        .align(Alignment.TopEnd)
                        .noRippleClick {
                            onDeleteClick()
                        }
                    ,
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = if (isClicked) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onBackground
                )
            }

        }

        if (isVisibilityText){
            val description = categoryUi.name.ifEmpty {
                CategoryList.getCategoryDescriptionById(
                    categoryUi.id.toLong()
                )
            }
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall
            )
        }

    }
}