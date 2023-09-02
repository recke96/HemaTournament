package info.marozzo.tournament.desktop.store

import com.arkivanov.mvikotlin.core.store.Store

internal interface TournamentStore : Store<TournamentStore.Intent, TournamentStore.State, Nothing> {

    sealed interface Intent {

    }

    sealed class State;

    data object LandingState : State();
}
