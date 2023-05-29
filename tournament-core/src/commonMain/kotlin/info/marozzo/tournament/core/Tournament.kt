package info.marozzo.tournament.core

import kotlinx.collections.immutable.ImmutableList

/**
 *  A [Tournament], played as part of an [Event].
 *
 *  The [Tournament.participants] should be a subset of the [Event.participants].
 */
data class Tournament(
    val name: String,
    val participants: ImmutableList<Participant>,
)
