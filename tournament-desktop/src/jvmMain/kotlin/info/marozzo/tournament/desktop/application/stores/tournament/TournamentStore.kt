package info.marozzo.tournament.desktop.application.stores.tournament

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory

internal interface TournamentStore : Store<TournamentIntent, TournamentState, Nothing> {
    companion object {
        fun create(
            storeFactory: StoreFactory,
            executorFactory: () -> TournamentExecutor,
            reducer: TournamentReducer,
        ): TournamentStore =
            object : TournamentStore, Store<TournamentIntent, TournamentState, Nothing> by storeFactory.create(
                name = "TournamentStore",
                initialState = NoEventState,
                executorFactory = executorFactory,
                reducer = reducer,
                autoInit = false,
            ) {}
    }
}
