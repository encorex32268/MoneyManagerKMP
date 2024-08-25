@file:OptIn(ExperimentalResourceApi::class)

package feature.chart.chartdetail.components

import androidx.compose.foundation.layout.Arrangement
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
    total: Long,
    typeId: Int,
    onBackClick: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = onBackClick
        ){
            Icon(
                imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                contentDescription = null
            )
        }
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            CircleIcon(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(36.dp)
                ,
                backgroundColor =  CategoryList.getColorByCategory(typeId),
                image = CategoryList.getTypeIconByTypeId(typeId),
                isClicked = true
            )
            Texts.TitleSmall(
                modifier = Modifier
                    .padding(start = 4.dp),
                text = CategoryList.getTypeStringByTypeId(typeId),
            )
            Spacer(Modifier.width(8.dp))
            AutoSizeText(
                modifier = Modifier
                    .padding(horizontal = 25.dp),
                text = total.toMoneyString(),
                style = TextStyle(
                    fontFamily = FontFamily(Font(Res.font.nunitosans_10pt_bold))
                )
            )
        }
    }
}

@Composable
private fun AutoSizeText(
    modifier: Modifier = Modifier,
    text: String,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    style: TextStyle,
    textSize: TextUnit = 24.sp
){
    var dynamicTextSize by remember {
        mutableStateOf(textSize)
    }
    Texts.TitleSmall(
        modifier = modifier,
        text = text,
        style = style.copy(
            fontSize = dynamicTextSize
        ),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        color = textColor,
        onTextLayout = {
            if (it.hasVisualOverflow && dynamicTextSize > 9.sp){
                dynamicTextSize = (dynamicTextSize.value - 1.0F).sp
            }
        }
    )
}