package info.marozzo.tournament.desktop.application.stores

import com.arkivanov.mvikotlin.core.store.Reducer
import kotlinx.datetime.Instant

data class TrySetStateMessage<State : Timestamped>(val newState: State, val oldTimestamp: Instant)

class OptimisticallyConcurrentReducer<State : Timestamped> :
    Reducer<State, TrySetStateMessage<State>> {
    override fun State.reduce(msg: TrySetStateMessage<State>): State =
        if (this.timestamp == msg.oldTimestamp) {
            msg.newState
        } else this
}
