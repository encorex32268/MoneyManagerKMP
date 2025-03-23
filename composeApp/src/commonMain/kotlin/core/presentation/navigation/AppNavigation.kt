package core.presentation.navigation

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource

@Composable
fun AppNavigationBottom(
    itemSelectedIndex: Int,
    onBarItemClick: (Int,String) -> Unit = { _ , _-> }
){
    NavigationBar(
        modifier = Modifier.height(60.dp)
    ) {
        bottomNavigationItems.forEachIndexed { index, bottomNavigationItem ->
            val isSelected = itemSelectedIndex == index
            NavigationBarItem(
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
                        contentDescription = bottomNavigationItem.name,
                        tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
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
        modifier = Modifier.width(60.dp)
    ) {
        bottomNavigationItems.forEachIndexed { index, bottomNavigationItem ->
            val isSelected = itemSelectedIndex == index
            NavigationRailItem(
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
                        contentDescription = bottomNavigationItem.name,
                        tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                }
            )
        }
    }

}