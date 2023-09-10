package info.marozzo.tournament.desktop.application.stores.tournament

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor

internal class TournamentExecutor :  CoroutineExecutor<TournamentIntent, Nothing, TournamentState, TournamentMessage, Nothing>() {
    override fun executeIntent(intent: TournamentIntent, getState: () -> TournamentState) {

    }
}
