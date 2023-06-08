package info.marozzo.tournament.core

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
