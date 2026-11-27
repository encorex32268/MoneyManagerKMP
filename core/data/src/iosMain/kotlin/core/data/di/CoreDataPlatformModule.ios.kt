package core.data.di

import core.data.database.DatabaseFactory
import core.data.datastore.DataStoreFactory
import org.koin.dsl.module

actual val coreDataPlatformModule = module {
    single { DatabaseFactory() }
    single { DataStoreFactory() }
}
