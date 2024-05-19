@file:OptIn(ExperimentalResourceApi::class)

package feature.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import feature.add.presentation.AddScreen
import feature.core.navigation.CustomTab
import feature.core.navigation.CustomTabOptions
import feature.core.presentation.CategoryList
import feature.core.presentation.components.CircleIcon
import feature.core.presentation.components.DatePicker
import feature.home.presentation.components.AmountTextLayout
import feature.home.presentation.components.ExpenseItem
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.baseline_receipt_24_filled
import moneymanagerkmp.composeapp.generated.resources.baseline_receipt_24_outline
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@ExperimentalResourceApi
object HomeTab : CustomTab {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val homeScreenModel = getScreenModel<HomeScreenModel>()
        val state by homeScreenModel.state.collectAsState()
        LaunchedEffect(Unit){
            println("LaunchedEffect Init")
            homeScreenModel.onEvent(
                HomeEvent.OnDatePick(
                    isInit = true
                )
            )
        }
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        navigator.parent?.push(AddScreen())
                    },
                    containerColor = MaterialTheme.colorScheme.onBackground
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription =null,
                        tint = MaterialTheme.colorScheme.background
                    )
                }
            }
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                DatePicker(
                    year = state.nowDateYear.toIntOrNull()?:0,
                    month = state.nowDateMonth.toIntOrNull()?:0,
                    onDateChange = { year , month ->
                        homeScreenModel.onEvent(
                            HomeEvent.OnDatePick(
                                year = year,
                                month = month
                            )
                        )
                    }
                )
                Spacer(modifier = Modifier.height(4.dp))
                AmountTextLayout(
                    modifier = Modifier.fillMaxWidth(),
                    income = state.income,
                    expense = state.expense,
                    total = state.totalAmount
                )
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ){
                items(
                    items = state.items
                ) {(_ , expenses) ->
                    ExpenseItem(
                        modifier = Modifier
                            .fillMaxWidth(),
                        items = expenses,
                        onItemClick = {

                        }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
            }
        }
    }

    override val customTabOptions: CustomTabOptions
        @Composable
        get() {
            val selectedIcon = painterResource(Res.drawable.baseline_receipt_24_filled)
            val unSelectedIcon = painterResource(Res.drawable.baseline_receipt_24_outline)
            return remember{
                CustomTabOptions(
                    index = 0u,
                    title = "Home",
                    selectedIcon = selectedIcon,
                    unSelectedIcon = unSelectedIcon
                )
            }
        }


}