@file:OptIn(ExperimentalResourceApi::class)

package feature.chart.presentation.chartdetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import feature.core.domain.model.Type
import feature.core.presentation.CategoryList
import feature.core.presentation.Texts
import feature.core.presentation.components.CircleIcon
import org.jetbrains.compose.resources.ExperimentalResourceApi

@Composable
fun ExpenseTypeTotal(
    modifier: Modifier = Modifier,
    type: Type,
    onBackClick: () -> Unit
) {
    Box(
        modifier = modifier
    ) {
        IconButton(
            modifier = Modifier.align(
                Alignment.CenterStart
            ),
            onClick = onBackClick
        ){
            Icon(
                imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                contentDescription = null
            )
        }
        Row(
            modifier = Modifier.align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically
        ){
            Box(
                modifier = Modifier.size(24.dp).background(
                    color = Color(type.colorArgb),
                    shape = RoundedCornerShape(8.dp)
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = type.name,
                style = MaterialTheme.typography.bodyLarge
            )

        }
    }
}

