package info.marozzo.tournament.desktop.application.stores.tournament

import arrow.fx.coroutines.ResourceScope
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.swing.Swing
import kotlinx.coroutines.withContext

internal interface TournamentStore : Store<TournamentIntent, TournamentState, Nothing> {
    companion object {

        private fun createInternal(
            storeFactory: StoreFactory,
            executorFactory: () -> TournamentExecutor,
            reducer: TournamentReducer,
        ): TournamentStore =
            object : TournamentStore, Store<TournamentIntent, TournamentState, Nothing> by storeFactory.create(
                name = "TournamentStore",
                initialState = NoEventState,
                executorFactory = executorFactory,
                reducer = reducer,
            ) {}

        context (ResourceScope)
        suspend fun create(
            storeFactory: StoreFactory,
            executorFactory: () -> TournamentExecutor,
            reducer: TournamentReducer,
        ): TournamentStore = install(
            {
                withContext(Dispatchers.Swing) { createInternal(storeFactory, executorFactory, reducer) }
            },
            { t, _ ->
                withContext(Dispatchers.Swing) { t.dispose() }
            }
        )
    }
}

