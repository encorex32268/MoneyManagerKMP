package core.presentation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.TabPosition
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.Dp

fun Modifier.customTabIndicatorOffset(
    currentTabPosition : TabPosition,
    tabWidth : Dp
) : Modifier = composed (
    inspectorInfo = debugInspectorInfo {
        name = "debugInspectorInfo"
        value = currentTabPosition
    }
){
    val currentTabWidth by animateDpAsState(
        targetValue = tabWidth,
        animationSpec = tween(
            durationMillis = 250,
            easing = FastOutSlowInEasing
        ), label = "currentTabWidth"
    )
    val indicatorOffset by animateDpAsState(
        targetValue = ((currentTabPosition.left + currentTabPosition.right - tabWidth)/2),
        animationSpec = tween(
            durationMillis = 250,
            easing = FastOutSlowInEasing
        ), label = "indicatorOffset"
    )
    fillMaxWidth()
        .wrapContentSize(Alignment.BottomStart)
        .offset(x = indicatorOffset)
        .width(currentTabWidth)
}


fun Modifier.noRippleClick(
    onClick: () -> Unit
): Modifier {
    return composed {
        clickable(
            indication = null,
            interactionSource = remember {
                MutableInteractionSource()
            },
            onClick = onClick
        )
    }
}