@file:OptIn(ExperimentalResourceApi::class)

package feature.home.presentation.add.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import core.presentation.CategoryList
import core.presentation.components.CategoryIconTile
import core.presentation.model.CategoryUi
import org.jetbrains.compose.resources.ExperimentalResourceApi

@Composable
fun CategoryItem(
    modifier: Modifier = Modifier,
    categoryUi: CategoryUi,
    isClicked: Boolean,
    onItemClick: (() -> Unit)? = null,
    isVisibilityText: Boolean = true,
    isNeedDeleteIcon: Boolean = false,
    onDeleteClick: () -> Unit = {},
) {
    val selectionColor = categoryUi.colorArgb?.let { Color(it) }
        ?: CategoryList.getColorByTypeId(categoryUi.typeId ?: 0L)
    val description = categoryUi.name.ifEmpty {
        CategoryList.getCategoryDescriptionById(categoryUi.id.toLong())
    }

    CategoryIconTile(
        modifier = modifier,
        icon = CategoryList.getCategoryIconById(categoryUi.id.toLong()),
        label = description,
        selected = isClicked,
        selectionColor = selectionColor,
        onClick = onItemClick,
        showLabel = isVisibilityText,
        showDeleteBadge = isNeedDeleteIcon,
        onDeleteClick = onDeleteClick,
    )
}
