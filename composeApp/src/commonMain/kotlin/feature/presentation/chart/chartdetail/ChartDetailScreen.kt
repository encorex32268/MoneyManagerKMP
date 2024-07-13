@file:OptIn(ExperimentalResourceApi::class)

package feature.presentation.chart.chartdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import feature.core.domain.model.Expense
import feature.core.presentation.CategoryList
import feature.core.presentation.Texts
import feature.core.presentation.components.CircleIcon
import feature.presentation.home.components.ExpenseItem
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.nunitosans_10pt_bold
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Font
import toMoneyString

class ChartDetailScreen(
    private val items: List<Expense>
): Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val chartDetailScreenModel = getScreenModel<ChartDetailScreenModel>()
        val state by chartDetailScreenModel.state.collectAsState()

        LaunchedEffect(Unit){
            chartDetailScreenModel.setupDetail(
                items = items
            )
        }
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(8.dp)
                .padding(bottom = 20.dp)
        ){
            ExpenseTypeTotal(
                modifier = Modifier
                    .fillMaxWidth()
                ,
                total = state.total,
                typeId = state.typeId,
                onBackClick = {
                    navigator.pop()
                }
            )
            state.items.forEach {
                ExpenseItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    items = it.second
                )
            }
        }
    }


}

@Composable
private fun ExpenseTypeTotal(
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