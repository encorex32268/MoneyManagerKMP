@file:OptIn(ExperimentalAdaptiveApi::class, ExperimentalResourceApi::class)

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import feature.analysis.presentation.AnalysisTab
import feature.chart.presentation.ChartTab
import feature.core.navigation.CustomTab
import feature.home.presentation.HomeTab
import feature.profile.presentation.ProfileTab
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveNavigationBar
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveNavigationBarItem
import io.github.alexzhirkevich.cupertino.adaptive.ExperimentalAdaptiveApi
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import moneymanagerkmp.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.mongodb.kbson.ObjectId

class Task : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var title : String = ""
    var isDone : Boolean = false
}

@OptIn(ExperimentalResourceApi::class)
@Composable
@Preview
fun App() {


//    val config = RealmConfiguration.create(
//        schema = setOf(Task::class)
//    )
//    val realm = Realm.open(config)

    AppTheme {
        TabNavigator(HomeTab){
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding()
                    .navigationBarsPadding(),
                bottomBar = {
                    NavigationBar(
                        containerColor = Color.Transparent
                    ) {
                        TabNavigationItem(HomeTab)
                        TabNavigationItem(ChartTab)
                        TabNavigationItem(AnalysisTab)
                        TabNavigationItem(ProfileTab)
                    }
                }
            ){
                Box(modifier = Modifier.fillMaxSize().padding(it)){
                    CurrentTab()
                }
            }
        }
    }
}

@Composable
private fun RowScope.TabNavigationItem(tab: CustomTab) {
    val tabNavigator = LocalTabNavigator.current
    val customTabOptions = tab.customTabOptions
    BottomNavigationItem(
        selected = tabNavigator.current == tab,
        onClick = {
            tabNavigator.current = tab
        },
        alwaysShowLabel = false,
        icon = {
            (if (tabNavigator.current == tab) customTabOptions.selectedIcon else customTabOptions.unSelectedIcon)?.let {
                Icon(
                    painter = it,
                    contentDescription = tab.options.title
                )
            }
        }
    )
}
