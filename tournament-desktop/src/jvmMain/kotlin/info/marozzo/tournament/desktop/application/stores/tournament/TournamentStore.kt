package info.marozzo.tournament.desktop.application.stores.tournament

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import org.koin.dsl.module

internal interface TournamentStore : Store<TournamentIntent, TournamentState, Nothing> {
    companion object {
        fun create(
            storeFactory: StoreFactory,
            executorFactory: () -> TournamentExecutor,
            reducer: TournamentReducer
        ): TournamentStore =
            object : TournamentStore, Store<TournamentIntent, TournamentState, Nothing> by storeFactory.create(
                name = "TournamentStore",
                initialState = LandingState,
                executorFactory = executorFactory,
                reducer = reducer
            ) {}
    }
}
