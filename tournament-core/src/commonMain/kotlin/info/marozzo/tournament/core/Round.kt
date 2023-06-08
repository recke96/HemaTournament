package info.marozzo.tournament.core

import kotlinx.collections.immutable.ImmutableList

/**
 * Represents a [Round] of matches to be played in a tournament.
 *
 * [Rounds][Round] are ordered / ranked by their [rank].
 */
data class Round(
    val rank: Ordinal,
    val matches: ImmutableList<Match>
)
