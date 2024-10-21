import androidx.compose.ui.window.ComposeUIViewController
import feature.chart.di.chartModule
import feature.di.appModule
import feature.home.di.homeModule
import org.koin.core.context.startKoin
import platform.UIKit.UIViewController

lateinit var IOSBanner: (String) -> UIViewController

fun generateIOSBanner(adUnitId: String): UIViewController {
    return IOSBanner(adUnitId)
}


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