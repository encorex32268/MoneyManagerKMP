import androidx.compose.ui.window.ComposeUIViewController
import feature.chart.di.chartModule
import feature.di.appModule
import feature.home.di.homeModule
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun MainViewController() = ComposeUIViewController(
    configure = {
        startKoin {
            modules(
                appModule,
                homeModule,
                chartModule
            )
        }
    }
){ App() }