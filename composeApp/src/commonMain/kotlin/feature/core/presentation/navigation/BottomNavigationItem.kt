@file:OptIn(ExperimentalResourceApi::class, ExperimentalResourceApi::class,
    ExperimentalResourceApi::class
)

package feature.core.presentation.navigation

import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.analytics
import moneymanagerkmp.composeapp.generated.resources.baseline_analytics_24_filled
import moneymanagerkmp.composeapp.generated.resources.baseline_analytics_24_outline
import moneymanagerkmp.composeapp.generated.resources.baseline_pie_chart_24_filled
import moneymanagerkmp.composeapp.generated.resources.baseline_pie_chart_24_outline
import moneymanagerkmp.composeapp.generated.resources.baseline_receipt_24_filled
import moneymanagerkmp.composeapp.generated.resources.baseline_receipt_24_outline
import moneymanagerkmp.composeapp.generated.resources.chart
import moneymanagerkmp.composeapp.generated.resources.home
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.StringResource

data class BottomNavigationItem(
    val selectedIcon: DrawableResource,
    val unSelectedIcon: DrawableResource,
    val name: String,
    val title: StringResource
)

const val HOME = "Route.Home"
const val CHART = "Route.Chart"
const val ANALYTICS = "Route.Analytics"

val bottomNavigationItems = listOf(
    BottomNavigationItem(
        selectedIcon = Res.drawable.baseline_receipt_24_filled,
        unSelectedIcon = Res.drawable.baseline_receipt_24_outline,
        name = HOME,
        title = Res.string.home
    ),
    BottomNavigationItem(
        selectedIcon = Res.drawable.baseline_pie_chart_24_filled,
        unSelectedIcon = Res.drawable.baseline_pie_chart_24_outline,
        name = CHART,
        title = Res.string.chart
    ),
    BottomNavigationItem(
        selectedIcon = Res.drawable.baseline_analytics_24_filled,
        unSelectedIcon = Res.drawable.baseline_analytics_24_outline,
        name = ANALYTICS,
        title = Res.string.analytics
    )
)
