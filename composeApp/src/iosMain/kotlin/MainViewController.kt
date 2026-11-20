import androidx.compose.ui.window.ComposeUIViewController
import app.presentation.App
import platform.UIKit.UIViewController

lateinit var IOSBanner: (String) -> UIViewController

fun generateIOSBanner(adUnitId: String): UIViewController {
    return IOSBanner(adUnitId)
}

fun MainViewController() = ComposeUIViewController(
    configure = {
        iosBannerFactory = IOSBanner
    }
) { App() }
