@file:OptIn(ExperimentalResourceApi::class, KoinExperimentalAPI::class)

package feature.chart.presentation.chartdetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import feature.chart.presentation.chartdetail.components.ExpenseTypeTotal
import feature.core.domain.model.Expense
import feature.core.domain.model.Type
import feature.core.presentation.Texts
import feature.home.presentation.components.ExpenseItem
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.nunitosans_10pt_bold
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Font
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.parameter.parametersOf
import toMoneyString

@Composable
fun DetailScreenRoot(
    items: List<Expense> = emptyList(),
    type: Type,
    viewModel: DetailViewModel = koinViewModel{
      parametersOf(items, type)
    },
    onBack: () -> Unit = {},
){
    val state by viewModel.state.collectAsStateWithLifecycle()

    DetailScreen(
        state = state,
        onBack = onBack
    )
}

@Composable
fun DetailScreen(
    state: DetailState,
    onBack: () -> Unit = {}
){
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(8.dp)
            .padding(bottom = 20.dp)
    ){
        ExpenseTypeTotal(
            modifier = Modifier
                .fillMaxWidth(),
            type = state.type,
            onBackClick = onBack
        )
        Spacer(modifier = Modifier.height(8.dp))
        AutoSizeText(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 25.dp),
            text = state.total.toMoneyString(),
            style = TextStyle(
                fontFamily = FontFamily(Font(Res.font.nunitosans_10pt_bold))
            )
        )

        state.items.forEach {
            ExpenseItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                items = it.second,
                isClick = false
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

