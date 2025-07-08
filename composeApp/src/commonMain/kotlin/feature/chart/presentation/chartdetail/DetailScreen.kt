@file:OptIn(ExperimentalResourceApi::class, KoinExperimentalAPI::class,
    ExperimentalMaterial3Api::class
)

package feature.chart.presentation.chartdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import core.domain.model.Expense
import core.domain.model.Type
import feature.home.presentation.components.ExpenseItem
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.expense_detail
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
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
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                title = {
                    Text(
                        text = stringResource(Res.string.expense_detail),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack
                    ){
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(8.dp)
                .padding(bottom = 20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ){
                Box(
                    modifier = Modifier.size(24.dp).background(
                        color = Color(state.type.colorArgb),
                        shape = RoundedCornerShape(8.dp)
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = state.type.name,
                    style = MaterialTheme.typography.bodyLarge
                )

            }
            AutoSizeText(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = state.total.toMoneyString()
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

}



@Composable
private fun AutoSizeText(
    modifier: Modifier = Modifier,
    text: String,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
){
    var dynamicTextSize by remember {
        mutableStateOf(24.sp)
    }
    Text(
        modifier = modifier,
        text = text,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        color = textColor,
        onTextLayout = {
            if (it.hasVisualOverflow && dynamicTextSize > 9.sp){
                dynamicTextSize = (dynamicTextSize.value - 1.0F).sp
            }
        },
        style = MaterialTheme.typography.labelLarge.copy(
            fontSize = dynamicTextSize
        )
    )
}

