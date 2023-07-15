package info.marozzo.tournament.desktop

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.store.create
import info.marozzo.tournament.core.Participant
import info.marozzo.tournament.core.matchgenerators.RoundRobinTournamentMatchGenerator
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.persistentMapOf

internal class TournamentStoreFactory(private val storeFactory: StoreFactory) {

    fun create(): TournamentStore =
        object : TournamentStore, Store<TournamentStore.Intent, TournamentStore.State, Nothing> by storeFactory.create(
            name = "TournamentStore",
            initialState = TournamentStore.State(
                persistentListOf(), RoundRobinTournamentMatchGenerator(), persistentMapOf()
            ),
            reducer = TournamentReducer,
        ) {}

    private object TournamentReducer : Reducer<TournamentStore.State, TournamentStore.Intent> {
        override fun TournamentStore.State.reduce(msg: TournamentStore.Intent): TournamentStore.State = when (msg) {
            is TournamentStore.Intent.AddParticipant -> copy(participants = participants.add(Participant(msg.name)))
            is TournamentStore.Intent.RemoveParticipant -> copy(participants = participants.remove(msg.participant))
            is TournamentStore.Intent.ChangeMatchGenerator -> copy(matchGenerator = msg.generator)
            is TournamentStore.Intent.EnterResult -> copy(matches = matches.put(msg.match, msg.result))
        }
    }
}
