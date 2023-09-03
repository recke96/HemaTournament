package info.marozzo.tournament.desktop.application.stores

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import info.marozzo.tournament.desktop.application.stores.tournament.TournamentExecutor
import info.marozzo.tournament.desktop.application.stores.tournament.TournamentReducer
import info.marozzo.tournament.desktop.application.stores.tournament.TournamentStore
import org.koin.dsl.module

val storesModule = module {
    single<StoreFactory> { DefaultStoreFactory() }
    factory { TournamentExecutor() }
    single { TournamentReducer }
    single<TournamentStore> { TournamentStore.create(get(), { get() }, get()) }
}
