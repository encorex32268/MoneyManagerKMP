package feature.chart.presentation

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import feature.core.navigation.CustomTab
import feature.core.navigation.CustomTabOptions
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.baseline_pie_chart_24_filled
import moneymanagerkmp.composeapp.generated.resources.baseline_pie_chart_24_outline
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

object ChartTab : CustomTab {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow


    }

    @OptIn(ExperimentalResourceApi::class)
    override val customTabOptions: CustomTabOptions
        @Composable
        get() {
            val selectedIcon = painterResource(Res.drawable.baseline_pie_chart_24_filled)
            val unSelectedIcon = painterResource(Res.drawable.baseline_pie_chart_24_outline)
            return remember{
                CustomTabOptions(
                    index = 1u,
                    title = "Chart",
                    selectedIcon = selectedIcon,
                    unSelectedIcon = unSelectedIcon
                )
            }
        }
}