package info.marozzo.tournament.core.domain

import kotlinx.collections.immutable.PersistentSet

@JvmInline
value class TournamentId(private val id: Int)

abstract class Tournament(override val id: TournamentId, val participants: PersistentSet<ParticipantId>): Entity<TournamentId>()

