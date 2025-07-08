package core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import app.presentation.LocalDarkLightMode
import core.presentation.noRippleClick
import core.ui.textColor

@Composable
fun CircleIcon(
    modifier: Modifier = Modifier,
    image: Painter?=null,
    isClicked: Boolean = false,
    id: Int?=null,
    onItemClick: (Int) -> Unit = {},
    backgroundColor: Color
) {
    val theme = LocalDarkLightMode.current
    Box(
        modifier = modifier
            .background(
                color = if (isClicked) backgroundColor else Color.Unspecified,
                shape = CircleShape
            )
            .clip(CircleShape)
            .noRippleClick {
                id?.let {
                    onItemClick(it)
                }
            },
        contentAlignment = Alignment.Center
    ){
        image?.let {
            Icon(
                modifier = Modifier
                    .size(50.dp)
                    .padding(8.dp),
                painter = image,
                contentDescription = null,
                tint =
                    when(theme){
                        true -> {
                            if (isClicked) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onBackground
                        }
                        else -> {
                            MaterialTheme.colorScheme.textColor()
                        }
                    }
            )
        }
    }

}


