package core.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource

@Composable
fun AppNavigationBottom(
    itemSelectedIndex: Int,
    onBarItemClick: (Int,String) -> Unit = { _ , _-> }
){
    NavigationBar(
        modifier = Modifier.height(60.dp),
        containerColor = MaterialTheme.colorScheme.background
    ) {
        bottomNavigationItems.forEachIndexed { index, bottomNavigationItem ->
            val isSelected = itemSelectedIndex == index
            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent),
                selected = isSelected,
                onClick = {
                    onBarItemClick(index , bottomNavigationItem.name)
                },
                icon = {
                    Icon(
                        painter = painterResource(
                            if (isSelected) {
                                bottomNavigationItem.selectedIcon
                            } else {
                                bottomNavigationItem.unSelectedIcon
                            }
                        ),
                        contentDescription = bottomNavigationItem.name
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
        modifier = Modifier.width(60.dp),
        containerColor = MaterialTheme.colorScheme.background
    ) {
        bottomNavigationItems.forEachIndexed { index, bottomNavigationItem ->
            val isSelected = itemSelectedIndex == index
            NavigationRailItem(
                colors = NavigationRailItemDefaults.colors(indicatorColor = Color.Transparent),
                selected = isSelected,
                onClick = {
                    onBarItemClick(index , bottomNavigationItem.name)
                },
                icon = {
                    Icon(
                        painter = painterResource(
                            if (isSelected) {
                                bottomNavigationItem.selectedIcon
                            } else {
                                bottomNavigationItem.unSelectedIcon
                            }
                        ),
                        contentDescription = bottomNavigationItem.name
                    )
                }
            )
        }
    }

}