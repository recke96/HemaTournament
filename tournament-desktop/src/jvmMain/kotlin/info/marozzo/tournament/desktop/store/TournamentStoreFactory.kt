package info.marozzo.tournament.desktop.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.collections.immutable.*

internal class TournamentStoreFactory(private val storeFactory: StoreFactory) {

    fun create(): TournamentStore =
        object : TournamentStore, Store<TournamentStore.Intent, TournamentStore.State, Nothing> by storeFactory.create(
            name = "TournamentStore",
            initialState = TournamentStore.LandingState,
            executorFactory = TournamentStoreFactory::TournamentExecutor,
            reducer = TournamentReducer,
        ) {}

    private sealed interface Msg {

    }

    private object TournamentReducer : Reducer<TournamentStore.State, Msg> {
        override fun TournamentStore.State.reduce(msg: Msg): TournamentStore.State = this
    }

    private class TournamentExecutor :
        CoroutineExecutor<TournamentStore.Intent, Nothing, TournamentStore.State, Msg, Nothing>() {
        override fun executeIntent(intent: TournamentStore.Intent, getState: () -> TournamentStore.State) {
        }
    }
}
