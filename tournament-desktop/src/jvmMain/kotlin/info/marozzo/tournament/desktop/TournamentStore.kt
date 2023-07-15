package info.marozzo.tournament.desktop

import com.arkivanov.mvikotlin.core.store.Store
import info.marozzo.tournament.core.Match
import info.marozzo.tournament.core.MatchResult
import info.marozzo.tournament.core.Participant
import info.marozzo.tournament.core.Round
import info.marozzo.tournament.core.matchgenerators.MatchGenerator
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.PersistentMap

internal interface TournamentStore : Store<TournamentStore.Intent, TournamentStore.State, Nothing> {

    sealed interface Intent {
        data class AddParticipant(val name: String) : Intent
        data class RemoveParticipant(val participant: Participant): Intent
        data class ChangeMatchGenerator(val generator: MatchGenerator): Intent
        data class EnterResult(val result: MatchResult<*>): Intent
    }

    data class State(
        val participants: PersistentList<Participant>,
        val matchGenerator: MatchGenerator,
        val rounds: ImmutableList<Round>,
        val results: PersistentMap<Match, MatchResult<*>?>
    )
}
