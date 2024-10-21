@file:OptIn(ExperimentalForeignApi::class)

import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import androidx.compose.ui.unit.dp
import com.lihan.moneymanager.BuildKonfig
import kotlinx.cinterop.ExperimentalForeignApi

@Composable
actual fun AdMobBanner(
    modifier: Modifier
) {
    UIKitView(
        factory = {
            generateIOSBanner(
                adUnitId = BuildKonfig.AdUnitID_iOS
            ).view
                  },
        modifier = modifier.height(50.dp),
    )
}