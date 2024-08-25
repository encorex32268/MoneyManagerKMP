@file:OptIn(ExperimentalResourceApi::class, KoinExperimentalAPI::class)

package feature.chart.chartdetail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import feature.chart.ChartViewModel
import feature.chart.chartdetail.components.ExpenseTypeTotal
import feature.core.domain.model.Expense
import feature.home.components.ExpenseItem
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@Composable
fun DetailScreenRoot(
    viewModel: DetailViewModel = koinViewModel(),
    items: List<Expense> = emptyList(),
    onBack: () -> Unit = {}
){
    val state by viewModel.state.collectAsState()
    LaunchedEffect(Unit){
        viewModel.setupDetail(
            items = items
        )
    }
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
                .fillMaxWidth()
            ,
            total = state.total,
            typeId = state.typeId,
            onBackClick = onBack
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




