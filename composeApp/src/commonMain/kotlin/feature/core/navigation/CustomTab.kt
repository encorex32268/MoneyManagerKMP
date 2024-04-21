package feature.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.Painter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import org.jetbrains.compose.resources.ExperimentalResourceApi

interface CustomTab: Tab {
    val customTabOptions: CustomTabOptions
        @Composable get

    override val options: TabOptions
        @Composable
        get(){
            val index = customTabOptions.index
            val title = customTabOptions.title
            return remember {
                TabOptions(
                    index = index,
                    title = title,
                    icon = null
                )
            }
        }
}

data class CustomTabOptions(
    val index: UShort,
    val title: String,
    val icon: Painter?=null,
    val selectedIcon: Painter? = null,
    val unSelectedIcon: Painter? = null
)
