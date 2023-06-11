package info.marozzo.tournament.core

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

/**
 * A [Match] that should be played as part of a tournament.
 */
data class Match(val rank: Ordinal, val a: Competitor, val b: Competitor)

/**
 * Replace all matching occurrences of [Competitor.WinnerOf] and [Competitor.LoserOf] according to the given [result].
 */
fun List<Round>.update(result: MatchResult<*>): ImmutableList<Round> = this.map { round ->
    round.copy(matches = round.matches.map { it.replaceWinnersAndLosers(result) }.toImmutableList())
}.toImmutableList()

/**
 *  Replace [Competitor.WinnerOf] and [Competitor.LoserOf] of the [Match] according to the given [result].
 */
fun Match.replaceWinnersAndLosers(result: MatchResult<*>): Match {
    val newA = a.resolve(result)
    val newB = b.resolve(result)

    if (a != newA || b != newB) {
        return Match(rank, newA, newB)
    }

    return this
}
