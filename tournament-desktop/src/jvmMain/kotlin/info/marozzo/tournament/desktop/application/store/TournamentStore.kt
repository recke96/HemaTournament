package info.marozzo.tournament.desktop.application.store

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import org.koin.dsl.module

internal interface TournamentStore : Store<TournamentIntent, TournamentState, Nothing>

val storeModule = module {
    single<StoreFactory> { DefaultStoreFactory() }
    factory { TournamentExecutor() }
    single { TournamentReducer }
    single<TournamentStore> {
        object : TournamentStore, Store<TournamentIntent, TournamentState, Nothing> by get<StoreFactory>().create(
            name = "TournamentStore",
            initialState = LandingState,
            executorFactory = { get<TournamentExecutor>() },
            reducer = get<TournamentReducer>()
        ){}
    }
}
