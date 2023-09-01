package info.marozzo.tournament.core.domain

@JvmInline
value class ParticipantId(private val id: Int)

data class Participant(override val id: ParticipantId, val name: Name) : Entity<ParticipantId>()
