package com.lihan.moneymanager.previews.home.add.type

import app.presentation.AppTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import feature.home.presentation.add.type.TypeUi
import feature.home.presentation.add.type.TypesScreen
import feature.home.presentation.add.type.TypesState
import kotlin.random.Random

@Preview
@Composable
fun TypeScreenPreview() {

    AppTheme {
        TypesScreen(
            state = TypesState(
                items = (0..5).map {
                    TypeUi(
                        typeIdTimestamp = it.toLong(),
                        name = "Type Ui ${it}",
                        colorArgb = Color(
                            red = Random.nextInt(0,255).toFloat(),
                            green = Random.nextInt(0,255).toFloat(),
                            blue = Random.nextInt(0,255).toFloat()
                        ).toArgb(),
                        order = it
                    )
                },
                itemsNotShowing = (0..5).map {
                    TypeUi(
                        typeIdTimestamp = it.toLong(),
                        name = "Type Ui ${it}",
                        colorArgb = Color(
                            red = Random.nextInt(0,255).toFloat(),
                            green = Random.nextInt(0,255).toFloat(),
                            blue = Random.nextInt(0,255).toFloat()
                        ).toArgb(),
                        order = it,
                        isShow = false
                    )
                },
                isSaving = false
            )
        )

    }

}

