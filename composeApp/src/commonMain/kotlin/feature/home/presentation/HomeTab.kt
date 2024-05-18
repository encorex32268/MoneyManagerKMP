@file:OptIn(ExperimentalResourceApi::class)

package feature.home.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import feature.core.navigation.CustomTab
import feature.core.navigation.CustomTabOptions
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.format.char
import kotlinx.datetime.offsetIn
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.app_name
import moneymanagerkmp.composeapp.generated.resources.baseline_receipt_24_filled
import moneymanagerkmp.composeapp.generated.resources.baseline_receipt_24_outline
import moneymanagerkmp.composeapp.generated.resources.compose_multiplatform
import moneymanagerkmp.composeapp.generated.resources.wear_bag
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@ExperimentalResourceApi
object HomeTab : CustomTab {

    @Composable
    override fun Content() {
        val screenModel = getScreenModel<HomeScreenModel>()
        val state by screenModel.state.collectAsState()

        
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            Button(
                onClick = {
                    screenModel.insertTestExpense()
                }
            ){
                Text("Insert")
            }
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = {
                    screenModel.getAll()
                }
            ){
                Text("Get All")
            }
            LazyColumn(
                modifier = Modifier.weight(1f)
            ){
                items(state.items){
                    Text(text = it.description)
                }
            }
            Image(
                painter = painterResource(
                    Res.drawable.wear_bag
                ),
                contentDescription = null
            )

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