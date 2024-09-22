import androidx.compose.ui.window.ComposeUIViewController
import feature.di.appModule
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun MainViewController() = ComposeUIViewController(
    configure = {
        startKoin {
            modules(
                appModule
            )
        }
    }
){ App() }