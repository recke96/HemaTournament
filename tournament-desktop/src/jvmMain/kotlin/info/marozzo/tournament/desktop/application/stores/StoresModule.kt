package info.marozzo.tournament.desktop.application.stores

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import info.marozzo.tournament.desktop.application.stores.tournament.tournamentModule
import org.koin.dsl.module

val storesModule = module {
    single<StoreFactory> { DefaultStoreFactory() }
    single { AcceptFunction(get()) }
    includes(tournamentModule)
}
