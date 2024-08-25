@file:OptIn(ExperimentalResourceApi::class, ExperimentalResourceApi::class,
    ExperimentalResourceApi::class
)

package feature.core.presentation.navigation

import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.baseline_pie_chart_24_filled
import moneymanagerkmp.composeapp.generated.resources.baseline_pie_chart_24_outline
import moneymanagerkmp.composeapp.generated.resources.baseline_receipt_24_filled
import moneymanagerkmp.composeapp.generated.resources.baseline_receipt_24_outline
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi

data class BottomNavigationItem(
    val selectedIcon: DrawableResource,
    val unSelectedIcon: DrawableResource,
    val name: String
)


val bottomNavigationItems = listOf(
    BottomNavigationItem(
        selectedIcon = Res.drawable.baseline_receipt_24_filled,
        unSelectedIcon = Res.drawable.baseline_receipt_24_outline,
        name = "Home"
    ),
    BottomNavigationItem(
        selectedIcon = Res.drawable.baseline_pie_chart_24_filled,
        unSelectedIcon = Res.drawable.baseline_pie_chart_24_outline,
        name = "Chart"
    )
)
