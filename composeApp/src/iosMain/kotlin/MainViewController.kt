import androidx.compose.ui.window.ComposeUIViewController
import app.presentation.App
import feature.di.sharedModules
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
                sharedModules
            )
        }
    }
){ App() }