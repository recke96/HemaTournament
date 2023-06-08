package info.marozzo.tournament.core.matchgenerators

import info.marozzo.tournament.core.*
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ensureActive
import kotlin.coroutines.coroutineContext
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
    override suspend fun generate(participants: ImmutableList<Participant>): ImmutableList<Round> {
        val competitors = participants
            .toSet()
            .map { Competitor.Fixed(it) }
            .shuffled(random)

        val n = competitors.size

        if (n < 2) {
            return persistentListOf()
        } else if (n == 2) {
            return persistentListOf(
                Round(
                    Ordinal.first,
                    persistentListOf(Match(Ordinal.first, competitors.first(), competitors.last()))
                )
            )
        }

        val roundCount = ceil(log2(n.toDouble())).toInt()
        val p = 1 shl roundCount
        val firstRoundCount = 2 * n - p

        var roundRank = Ordinal.first
        var rounds = persistentListOf<Round>()

        val firstRoundCompetitors = competitors.subList(0, firstRoundCount)

        val matches = firstRoundCompetitors
            .asSequence()
            .windowed(size = 2, step = 2)
            .zip(Ordinal.sequence) { (a, b), rank -> Match(rank, a, b) }
            .toImmutableList()

        rounds = rounds.add(Round(roundRank, matches))
        roundRank = roundRank.next()

        var nextCompetitors =
            (competitors.subList(firstRoundCount, n) + matches.map { Competitor.WinnerOf(it) }).shuffled(random)

        for (i in 0 until (roundCount - 1)) {
            coroutineContext.ensureActive()

            val roundMatches = nextCompetitors
                .asSequence()
                .windowed(size = 2, step = 2)
                .zip(Ordinal.sequence) { (a, b), rank -> Match(rank, a, b) }
                .toImmutableList()

            rounds = rounds.add(Round(roundRank, roundMatches))
            roundRank = roundRank.next()

            nextCompetitors = roundMatches.map { Competitor.WinnerOf(it) }.shuffled(random)
        }

        return rounds
    }
}
