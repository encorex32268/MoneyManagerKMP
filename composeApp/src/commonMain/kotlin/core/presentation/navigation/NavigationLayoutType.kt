package core.presentation.navigation

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Immutable


@Immutable
enum class NavigationLayoutType {
    BOTTOM_NAVIGATION,
    NAVIGATION_RAIL,
    FULL_SCREEN,
}

fun WindowSizeClass.calculateNavigationLayout(currentRoute: String?): NavigationLayoutType {
    return when (widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            NavigationLayoutType.BOTTOM_NAVIGATION
        }

        else                         -> {
            NavigationLayoutType.NAVIGATION_RAIL
        }
    }
}