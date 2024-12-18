package feature.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import feature.core.presentation.noRippleClick

@Composable
fun CircleIcon(
    modifier: Modifier = Modifier,
    image: Painter?=null,
    isClicked: Boolean = false,
    id: Int?=null,
    onItemClick: (Int) -> Unit = {},
    backgroundColor: Color,
    colorCheck: Boolean = false
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = if (isClicked) backgroundColor else Color.White,
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
                val tintColor = remember(backgroundColor){
                    if (backgroundColor.luminance() < 0.5f && colorCheck) Color(0xFFFDFDFD) else Color.Black
                }
                Icon(
                    modifier = Modifier
                        .size(50.dp)
                        .padding(8.dp),
                    painter = image,
                    contentDescription = null,
                    tint = tintColor,
                )
            }
        }
    }

}


