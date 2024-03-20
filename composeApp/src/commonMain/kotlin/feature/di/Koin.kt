package feature.di

import feature.core.data.model.CategoryEntity
import feature.core.data.model.ExpenseEntity
import feature.home.data.repository.HomeRepositoryImpl
import feature.home.domain.repository.HomeRepository
import feature.home.presentation.HomeScreenModel
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val dataModule = module {

    single<Realm>{
        val config = RealmConfiguration.create(
           schema = setOf(
               ExpenseEntity::class,
               CategoryEntity::class
           )
        )
        Realm.open(config)
    }


    single<HomeRepository>{
        HomeRepositoryImpl(get())
    }


//    single {
//        val json = Json { ignoreUnknownKeys = true }
//        HttpClient {
//            install(ContentNegotiation) {
//                // TODO Fix API so it serves application/json
//                json(json, contentType = ContentType.Any)
//            }
//        }
//    }

//    single<MuseumApi> { KtorMuseumApi(get()) }
//    single<MuseumStorage> { InMemoryMuseumStorage() }
//    single {
//        MuseumRepository(get(), get()).apply {
//            initialize()
//        }
//    }
}


val screenModelsModule = module {
    factoryOf(::HomeScreenModel)
}

fun initKoin() {
    startKoin {
        modules(
            dataModule,
            screenModelsModule
        )
    }
}
