package info.marozzo.tournament.core.matchgenerators

import info.marozzo.tournament.core.Competitor
import info.marozzo.tournament.core.Match
import info.marozzo.tournament.core.Participant
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlin.math.ceil
import kotlin.math.log2
import kotlin.random.Random

/**
 * [MatchGenerator] for single-elimination tournaments.
 *
 * Does *not* support seeding.
 * Does support a number of [Participants][Participant] that is *unequal* to a power of two.
 */
class SingleEliminationTournamentMatchGenerator(private val random: Random = Random) : MatchGenerator {
    override fun generate(participants: ImmutableList<Participant>): ImmutableList<Match> {
        val competitors = participants
            .toSet()
            .map { Competitor.Fixed(it) }
            .shuffled(random)

        val n = competitors.size

        if (n < 2) {
            return persistentListOf()
        } else if (n == 2) {
            return persistentListOf(Match(competitors.first(), competitors.last()))
        }

        val rounds = ceil(log2(n.toDouble())).toInt()
        val p = 1 shl rounds
        val firstRoundCount = 2 * n - p

        val firstRoundCompetitors = competitors.subList(0, firstRoundCount)

        val matches = firstRoundCompetitors.windowed(size = 2, step = 2) { (a, b) -> Match(a, b) }.toMutableList()
        var nextCompetitors =
            (competitors.subList(firstRoundCount, n) + matches.map { Competitor.WinnerOf(it) }).shuffled(random)

        for (i in 0 until (rounds - 1)) {
            val roundMatches = nextCompetitors.windowed(size = 2, step = 2) { (a, b) -> Match(a, b) }
            matches.addAll(roundMatches)

            nextCompetitors = roundMatches.map { Competitor.WinnerOf(it) }.shuffled(random)
        }

        return matches.toImmutableList()
    }
}
