@file:OptIn(ExperimentalForeignApi::class)

import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import androidx.compose.ui.unit.dp
import com.lihan.moneymanager.BuildKonfig
import kotlinx.cinterop.ExperimentalForeignApi
import platform.UIKit.UIView
import platform.UIKit.UIViewController

var iosBannerFactory: ((String) -> UIViewController)? = null

@Composable
actual fun AdMobBanner(
    modifier: Modifier
) {
    UIKitView(
        factory = {
            iosBannerFactory?.invoke(BuildKonfig.AdUnitID_iOS)?.view ?: UIView()
        },
        modifier = modifier.height(50.dp),
    )
}
