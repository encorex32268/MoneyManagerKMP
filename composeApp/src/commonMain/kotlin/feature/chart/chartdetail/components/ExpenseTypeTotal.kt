@file:OptIn(ExperimentalResourceApi::class)

package feature.chart.chartdetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import feature.core.presentation.CategoryList
import feature.core.presentation.Texts
import feature.core.presentation.components.CircleIcon
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.nunitosans_10pt_bold
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Font
import toMoneyString

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
                backgroundColor =  CategoryList.getColorByCategory(typeId),
                image = CategoryList.getTypeIconByTypeId(typeId),
                isClicked = true
            )
            Spacer(modifier = Modifier.width(8.dp))
            Texts.TitleSmall(
                text = CategoryList.getTypeStringByTypeId(typeId),
            )

        }
    }
}

