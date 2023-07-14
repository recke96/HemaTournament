package info.marozzo.tournament.desktop

import com.arkivanov.mvikotlin.core.store.Store
import info.marozzo.tournament.core.Participant
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.PersistentList

internal interface TournamentStore : Store<TournamentStore.Intent, TournamentStore.State, Nothing> {

    sealed interface Intent {
        data class AddParticipant(val name: String) : Intent

    }

    data class State(
        val participants: PersistentList<Participant>
    )
}
