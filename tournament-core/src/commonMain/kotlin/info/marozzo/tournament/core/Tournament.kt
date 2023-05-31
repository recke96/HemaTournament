package info.marozzo.tournament.core

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
 * A [Competitor] of a [Match].
 *
 * Can be [Fixed], i.e. known from the start or depend on the outcome of a previous [Match]
 * ([WinnerOf], [LoserOf]).
 */
sealed class Competitor {
    data class Fixed(val participant: Participant): Competitor()
    data class WinnerOf(val match: Match): Competitor()
    data class LoserOf(val match: Match): Competitor()
}
