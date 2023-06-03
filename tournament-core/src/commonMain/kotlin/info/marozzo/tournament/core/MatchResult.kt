package info.marozzo.tournament.core

/**
 * Represents the result of a [Match] using [Comparable] scores for each player.
 *
 * If `[a] > [b]` then [Match.a] has won.
 * If `[b] > [a]` then [Match.b] has won.
 * If `[a] == [b]` then the match was a draw.
 */
interface MatchResult<T> where T : Comparable<T> {
    val match: Match
    val a: T
    val b: T
}

/**
 * Value class for the number of points awarded to a [Competitor].
 */
@JvmInline
value class Points(val points: Int) {
    init {
        require(points >= 0) { "Points can't be negative, but were $points" }
    }
}

/**
 * Value class for a number of hits in a [Match].
 */
@JvmInline
value class Hits(val hits: Int) {
    init {
        require(hits >= 0) { "Hits can't be negative, but were $hits" }
    }
}

/**
 * A [MatchResult] as recommended by the [Tournament Rules of 'Fior della Spada'](https://www.marozzo.info/wp-content/uploads/2023/03/Regelwerk-Fior-della-Spada-Stand-2022b.pdf).
 *
 * Tracks the number of points by each [Competitor] and the number of double hits.
 * The score for a player is calculated as follows: \(S_{player} = \dfrac{P_{player}}{1 + P_{opponent}}\).
 * If the [double hits][doubles] reach `3` then both [Competitors][Competitor] receive a score of `0`
 * and the match is considered a draw.
 *
 * @usesMathJax
 */
data class CutScoreResult(override val match: Match, val pointsByA: Points, val pointsByB: Points, val doubles: Hits) :
    MatchResult<Double> {

    companion object {
        fun cut(player: Points, opponent: Points, doubles: Hits): Double =
            if (doubles.hits >= 3.0) 0.0 else player.points.toDouble() / (1.0 + opponent.points.toDouble())
    }

    override val a: Double = cut(pointsByA, pointsByB, doubles)
    override val b: Double = cut(pointsByB, pointsByA, doubles)
}
