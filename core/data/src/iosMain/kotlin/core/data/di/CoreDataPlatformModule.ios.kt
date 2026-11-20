package core.data.di

import core.data.database.DatabaseFactory
import org.koin.dsl.module

actual val coreDataPlatformModule = module {
    single { DatabaseFactory() }
}
