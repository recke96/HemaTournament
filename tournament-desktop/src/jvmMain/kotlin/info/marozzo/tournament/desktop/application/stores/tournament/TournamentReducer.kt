package info.marozzo.tournament.desktop.application.stores.tournament

import com.arkivanov.mvikotlin.core.store.Reducer

internal object TournamentReducer: Reducer<TournamentState, TournamentMessage> {
    override fun TournamentState.reduce(msg: TournamentMessage): TournamentState {
        return this
    }
}
