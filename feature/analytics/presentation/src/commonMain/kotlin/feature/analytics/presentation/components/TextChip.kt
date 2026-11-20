package feature.analytics.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun TextChip(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    isSelected: Boolean = false,
    text: String
){
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable {
                onClick()
            }
            .background(
                shape = RoundedCornerShape(16.dp),
                color = if (isSelected) {
                    MaterialTheme.colorScheme.onBackground
                }else{
                    MaterialTheme.colorScheme.background
                }
            ),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = text,
            color = if (!isSelected) {
                MaterialTheme.colorScheme.onBackground
            }else{
                MaterialTheme.colorScheme.background
            },
            style = MaterialTheme.typography.titleSmall
        )
    }
}

