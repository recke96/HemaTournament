package info.marozzo.tournament.desktop

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import info.marozzo.tournament.core.MatchResult
import info.marozzo.tournament.core.Participant
import info.marozzo.tournament.core.Round
import info.marozzo.tournament.core.matchgenerators.MatchGenerator
import info.marozzo.tournament.core.matchgenerators.RoundRobinTournamentMatchGenerator
import kotlinx.collections.immutable.*
import kotlinx.coroutines.launch

internal class TournamentStoreFactory(private val storeFactory: StoreFactory) {

    fun create(): TournamentStore =
        object : TournamentStore, Store<TournamentStore.Intent, TournamentStore.State, Nothing> by storeFactory.create(
            name = "TournamentStore",
            initialState = TournamentStore.State(
                persistentListOf(), RoundRobinTournamentMatchGenerator(), persistentListOf(), persistentMapOf()
            ),
            executorFactory = ::TournamentExecutor,
            reducer = TournamentReducer,
        ) {}

    private sealed interface Msg {
        data class UpdateTournament(
            val participants: PersistentList<Participant>,
            val generator: MatchGenerator,
            val rounds: ImmutableList<Round>
        ) : Msg

        data class EnterResult(val result: MatchResult<*>) : Msg
    }

    private object TournamentReducer : Reducer<TournamentStore.State, Msg> {
        override fun TournamentStore.State.reduce(msg: Msg): TournamentStore.State = when (msg) {
            is Msg.UpdateTournament -> copy(participants = msg.participants,
                matchGenerator = msg.generator,
                rounds = msg.rounds,
                results = msg.rounds.flatMap { it.matches }.associateWith { null }.toPersistentMap()
            )

            is Msg.EnterResult -> copy(results = results.put(msg.result.match, msg.result))
        }
    }

    private class TournamentExecutor :
        CoroutineExecutor<TournamentStore.Intent, Nothing, TournamentStore.State, Msg, Nothing>() {
        override fun executeIntent(intent: TournamentStore.Intent, getState: () -> TournamentStore.State) {
            val (participants, generator, _) = getState()

            when (intent) {
                is TournamentStore.Intent.AddParticipant -> regenerateMatches(
                    participants.add(Participant(intent.name)), generator
                )

                is TournamentStore.Intent.RemoveParticipant -> regenerateMatches(
                    participants.remove(intent.participant), generator
                )

                is TournamentStore.Intent.ChangeMatchGenerator -> regenerateMatches(participants, intent.generator)
                is TournamentStore.Intent.EnterResult -> dispatch(Msg.EnterResult(intent.result))
            }
        }

        private fun regenerateMatches(participants: PersistentList<Participant>, matchGenerator: MatchGenerator) =
            scope.launch {
                val rounds = matchGenerator.generate(participants)
                dispatch(Msg.UpdateTournament(participants, matchGenerator, rounds))
            }
    }
}
