package info.marozzo.tournament.core

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.mutate
import kotlinx.collections.immutable.toPersistentList

/**
 * A [Match] that should be played.
 *
 * This does not imply home / away positions of [Competitors][Competitor] but is an unordered pair.
 * This means `([a], [b]) == ([b], [a])`.
 */
data class Match(val a: Competitor, val b: Competitor) {

    /**
     * A [Match] is equal if the [Competitors][Competitor] are the same,
     * so ([a], [b]) == ([b], [a]).
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }

        if (other is Match) {
            return (a == other.a && b == other.b) || (a == other.b && b == other.a)
        }

        return false
    }

    /**
     * A [hashCode]-implementation that fulfills the contract ([a], [b]) == ([b], [a]).
     */
    override fun hashCode(): Int = 31 + (a.hashCode() * b.hashCode())
}

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
        return Match(newA, newB)
    }

    return this
}
