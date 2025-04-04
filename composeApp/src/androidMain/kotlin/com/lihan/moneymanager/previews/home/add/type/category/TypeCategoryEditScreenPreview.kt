package com.lihan.moneymanager.previews.home.add.type.category

import app.presentation.AppTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import core.domain.mapper.toCategoryUi
import core.presentation.CategoryList
import feature.home.presentation.add.type.TypeUi
import feature.home.presentation.add.type.category.TypeCategoryEditScreen
import feature.home.presentation.add.type.category.TypeCategoryEditState

@Preview
@Composable
fun TypeCategoryEditScreenPreview() {
    AppTheme {
        TypeCategoryEditScreen(
            state = TypeCategoryEditState(
                categories = CategoryList.items.subList(0,20).map { it.toCategoryUi() },
                typeUi = TypeUi(
                    typeIdTimestamp = 0,
                    name = "EditTypeUi",
                    colorArgb = Color.Red.copy(alpha = 0.5f).toArgb(),
                    order = 1,
                    categories = CategoryList.items.subList(20,25).map { it.toCategoryUi() }
                )
            )
        )
    }

}
