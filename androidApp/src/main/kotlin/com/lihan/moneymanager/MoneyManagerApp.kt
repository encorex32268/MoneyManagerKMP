package com.lihan.moneymanager

import android.app.Application
import app.di.initKoin
import org.koin.android.ext.koin.androidContext

class MoneyManagerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@MoneyManagerApp)
        }
    }
}
