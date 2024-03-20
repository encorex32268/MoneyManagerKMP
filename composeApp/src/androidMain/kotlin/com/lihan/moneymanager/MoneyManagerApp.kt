package com.lihan.moneymanager

import android.app.Application
import feature.di.initKoin

class MoneyManagerApp: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }
}