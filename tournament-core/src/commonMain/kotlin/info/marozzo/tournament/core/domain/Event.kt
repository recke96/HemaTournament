package info.marozzo.tournament.core.domain

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import kotlinx.collections.immutable.PersistentSet
import kotlinx.collections.immutable.persistentSetOf

@JvmInline
value class EventId(private val id: Int)

@AggregateRoot
data class Event(
    override val id: EventId,
    val participants: PersistentSet<Participant> = persistentSetOf()
) : Entity<EventId>()

fun Event.addParticipant(name: String, nextId: () -> ParticipantId): Either<Error.Validation, Event> = either {
    val validName = Name(name).bind()
    val participant = Participant(nextId(), validName)
    copy(participants = participants.add(participant))
}

fun Event.removeParticipant(id: ParticipantId): Either<Error.NotFound<ParticipantId>, Event> = either {
    val participant = participants.find { it.id == id }
    ensure(participant != null) { Error.NotFound(id) }
    copy(participants = participants.remove(participant))
}
