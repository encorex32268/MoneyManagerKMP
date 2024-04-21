package feature.chart.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
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
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            Text("ChartTab")
        }

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