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

/**
 * Resolves undecided [Competitors][Competitor] such as [Competitor.WinnerOf] and [Competitor.LoserOf] with the given [result] if possible.
 *
 * @exception IllegalStateException Thrown if a replacement was requested, but the [result] is a draw.
 */
fun Competitor.resolve(result: MatchResult<*>): Competitor = when (this) {
    is Competitor.Fixed -> this
    is Competitor.WinnerOf -> if (match == result.match) result.winner else this
    is Competitor.LoserOf -> if (match == result.match) result.loser else this
}

/**
 * A [Competitor] of a [Match].
 *
 * Can be [Fixed], i.e. known from the start or depend on the outcome of a previous [Match]
 * ([WinnerOf], [LoserOf]).
 */
sealed class Competitor {
    data class Fixed(val participant: Participant) : Competitor()
    data class WinnerOf(val match: Match) : Competitor()
    data class LoserOf(val match: Match) : Competitor()
}
