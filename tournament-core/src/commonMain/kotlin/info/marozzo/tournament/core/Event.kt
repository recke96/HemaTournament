package info.marozzo.tournament.core

import kotlinx.collections.immutable.ImmutableList

/**
 * Represents a [Event], possibly consisting of multiple [Tournament]s.
 */
data class Event(
    val name: String,
    val participants: ImmutableList<Participant>,
    val tournaments: ImmutableList<Tournament>,
)
