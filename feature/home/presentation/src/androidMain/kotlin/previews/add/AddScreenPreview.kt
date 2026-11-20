package com.lihan.moneymanager.previews.home.add

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import app.presentation.AppTheme
import core.domain.mapper.toCategory
import core.domain.mapper.toCategoryUi
import core.domain.mapper.toTypeUi
import core.domain.model.Type
import core.presentation.CategoryList
import feature.home.presentation.add.AddScreen
import feature.home.presentation.add.AddState
import feature.home.presentation.add.components.CalculateLayout

@Preview
@Composable
fun AddScreenPreview() {
    val types = mutableListOf<Type>()
    val defaultCategories = CategoryList.items.groupBy { it.typeId }
    defaultCategories.onEachIndexed { index, entry ->
        val typeId = entry.key?:0
        val typeName = CategoryList.getTypeStringByTypeId(typeId = typeId)
        val typeColor = CategoryList.getColorByTypeId(id = typeId).toArgb()
        types.add(
            Type(
                typeIdTimestamp = typeId,
                name = typeName,
                colorArgb = typeColor,
                order = index,
                categories = entry.value.mapIndexed { index, category ->
                    val description = CategoryList.getTypeStringByTypeId(category.id.toLong())
                    category.copy(
                        order = index,
                        name = description
                    )
                }
            )
        )
    }

    AppTheme {
        AddScreen(
            state = AddState(
                types = types.map { it.toTypeUi() }
            ),
            isDebug = true,

        )
    }

}

@Preview(showSystemUi = true)
@Composable
fun CalculateLayoutPreview() {
    val types = mutableListOf<Type>()
    val defaultCategories = CategoryList.items.groupBy { it.typeId }
    defaultCategories.onEachIndexed { index, entry ->
        val typeId = entry.key?:0
        val typeName = CategoryList.getTypeStringByTypeId(typeId = typeId)
        val typeColor = CategoryList.getColorByTypeId(id = typeId).toArgb()
        types.add(
            Type(
                typeIdTimestamp = typeId,
                name = typeName,
                colorArgb = typeColor,
                order = index,
                categories = entry.value.mapIndexed { index, category ->
                    val description = CategoryList.getTypeStringByTypeId(category.id.toLong())
                    category.copy(
                        order = index,
                        name = description
                    )
                }
            )
        )
    }
    val uiTypes = types.map { it.toTypeUi() }

    AppTheme {
        CalculateLayout(
            modifier = Modifier.background(Color.White),
            state = AddState(
                types =uiTypes,
                categoryUi = uiTypes[0].categories[0].toCategory().toCategoryUi(),
                description = "Test description",
                cost = "9992"
            ),
            month = "11",
            day = "21"
        )
    }

}
