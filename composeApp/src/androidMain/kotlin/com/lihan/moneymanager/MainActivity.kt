package com.lihan.moneymanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import app.presentation.App
import com.google.android.gms.ads.MobileAds
import io.github.vinceglb.filekit.core.FileKit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        MobileAds.initialize(this)
        FileKit.init(this)
        setContent {
            App(
                onModeChanged = { isDarkMode ->
                    WindowCompat.getInsetsController(window, window.decorView)
                        .isAppearanceLightStatusBars = !isDarkMode
                }
            )
        }
    }
}