@file:OptIn(ExperimentalResourceApi::class)

package feature.home.add.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import feature.core.presentation.CategoryList
import feature.core.presentation.Texts
import feature.core.presentation.components.CircleIcon
import feature.core.presentation.model.CategoryUi
import org.jetbrains.compose.resources.ExperimentalResourceApi

@Composable
fun CategoryItem(
    modifier: Modifier = Modifier,
    categoryUi: CategoryUi,
    isClicked: Boolean,
    onItemClick: (() -> Unit)? = null
){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircleIcon(
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .padding(top = 8.dp)
                .size(48.dp)
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

        val description = categoryUi.name.ifEmpty {
            CategoryList.getCategoryDescriptionById(
                categoryUi.id.toLong()
            )
        }
        Texts.BodySmall(
            text = description
        )

    }
}