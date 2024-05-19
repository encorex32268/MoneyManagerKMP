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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

@Composable
fun CircleIcon(
    modifier: Modifier = Modifier,
    image: Painter,
    isClicked: Boolean = false,
    id: Int?=null,
    onItemClick: (Int) -> Unit = {},
    backgroundColor: Color,
    tintColor: Color = Color.Unspecified
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
                .clickable {
                    id?.let {
                        onItemClick(it)
                    }
                },
            contentAlignment = Alignment.Center
        ){
            Icon(
                modifier = Modifier
                    .size(50.dp)
                    .padding(8.dp),
                painter = image,
                contentDescription = null,
                tint =  if (backgroundColor.luminance() < 0.5f && isClicked) Color.White else Color.Black,
            )
        }
    }

}


