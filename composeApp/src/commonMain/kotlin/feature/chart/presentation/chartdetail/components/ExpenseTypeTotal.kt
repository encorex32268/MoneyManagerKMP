@file:OptIn(ExperimentalResourceApi::class)

package feature.chart.presentation.chartdetail.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import feature.core.presentation.CategoryList
import feature.core.presentation.Texts
import feature.core.presentation.components.CircleIcon
import org.jetbrains.compose.resources.ExperimentalResourceApi

@Composable
fun ExpenseTypeTotal(
    modifier: Modifier = Modifier,
    typeId: Int,
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
            CircleIcon(
                modifier = Modifier.size(36.dp),
                backgroundColor =  CategoryList.getColorByTypeId(typeId.toLong()),
                isClicked = true
            )
            Spacer(modifier = Modifier.width(8.dp))
            Texts.TitleSmall(
                text = CategoryList.getTypeStringByTypeId(typeId.toLong()),
            )

        }
    }
}

