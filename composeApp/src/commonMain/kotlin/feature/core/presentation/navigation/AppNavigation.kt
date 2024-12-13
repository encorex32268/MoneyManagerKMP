package feature.core.presentation.navigation

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource

@Composable
fun AppNavigationBottom(
    itemSelectedIndex: Int,
    onBarItemClick: (Int,String) -> Unit = { _ , _-> }
){
    NavigationBar(
        containerColor = Color.White,
        modifier = Modifier.height(60.dp)
    ) {
        bottomNavigationItems.forEachIndexed { index, bottomNavigationItem ->
            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.LightGray.copy(alpha = 0.3f)
                ),
                selected = (itemSelectedIndex == index),
                onClick = {
                    onBarItemClick(index , bottomNavigationItem.name)
                },
                icon = {
                    Icon(
                        painter = painterResource(
                            if (itemSelectedIndex == index) {
                                bottomNavigationItem.selectedIcon
                            } else {
                                bottomNavigationItem.unSelectedIcon
                            }
                        ),
                        contentDescription = bottomNavigationItem.name,
                    )
                }
            )
        }
    }

}

@Composable
fun AppNavigationRail(
    itemSelectedIndex: Int,
    onBarItemClick: (Int,String) -> Unit = { _ , _-> }
){

    NavigationRail(
        containerColor = Color.White,
        modifier = Modifier.width(60.dp)
    ) {
        bottomNavigationItems.forEachIndexed { index, bottomNavigationItem ->
            NavigationRailItem(
                colors = NavigationRailItemDefaults.colors(
                    indicatorColor = Color.LightGray.copy(alpha = 0.3f)
                ),
                selected = (itemSelectedIndex == index),
                onClick = {
                    onBarItemClick(
                        index,
                        bottomNavigationItem.name
                    )
                },
                icon = {
                    Icon(
                        painter = painterResource(
                            if (itemSelectedIndex == index) {
                                bottomNavigationItem.selectedIcon
                            } else {
                                bottomNavigationItem.unSelectedIcon
                            }
                        ),
                        contentDescription = bottomNavigationItem.name,
                    )
                }
            )
        }
    }

}