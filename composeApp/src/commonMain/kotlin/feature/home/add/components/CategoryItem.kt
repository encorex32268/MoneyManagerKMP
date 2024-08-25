@file:OptIn(ExperimentalResourceApi::class)

package feature.home.add.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    onItemClick: () -> Unit = {}
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
            backgroundColor = CategoryList.getColorByCategory(categoryUi.typeId),
            image = CategoryList.getCategoryIconById(categoryUi.categoryId),
            isClicked = isClicked,
            id = categoryUi.categoryId,
            onItemClick = {
                onItemClick()
            },
        )

        val description = categoryUi.name?: CategoryList.getCategoryDescriptionById(
            categoryUi.categoryId
        )
        Texts.BodySmall(text = description )

    }
}