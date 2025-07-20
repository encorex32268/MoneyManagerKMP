package com.lihan.moneymanager

import android.app.Application
import feature.di.sharedModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MoneyManagerApp: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MoneyManagerApp)
            modules(
                sharedModules
            )
        }
    }
}

