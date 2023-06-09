package info.marozzo.tournament.core

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.mutate
import kotlinx.collections.immutable.toPersistentList

/**
 * A [Match] that should be played as part of a tournament.
 */
data class Match(val rank: Ordinal, val a: Competitor, val b: Competitor)

/**
 * Replace all matching occurrences of [Competitor.WinnerOf] and [Competitor.LoserOf] according to the given [result].
 */
fun List<Match>.update(result: MatchResult<*>): ImmutableList<Match> = this.toPersistentList().mutate {
    it.replaceAll { match -> match.replaceWinnersAndLosers(result) }
}

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
