package feature.analytics.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import feature.analytics.presentation.DateFilter
import feature.core.presentation.Texts

@Composable
fun FilterChip(
    modifier: Modifier = Modifier,
    dateFilter: DateFilter,
    onClick: () -> Unit,
    isSelected: Boolean = false
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable {
                onClick()
            }
            .background(
                shape = RoundedCornerShape(16.dp),
                color = if (isSelected) {
                    Color.Black
                }else{
                    Color.White
                }
            )
        ,
        contentAlignment = Alignment.Center
    ){
        Text(
            text = dateFilter.text,
            color = if (!isSelected) {
                Color.Black
            }else{
                Color.White
            },
            style = MaterialTheme.typography.titleSmall
        )
    }

}

