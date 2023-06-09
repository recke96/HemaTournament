package info.marozzo.tournament.core.matchgenerators

import info.marozzo.tournament.core.*
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ensureActive
import kotlin.coroutines.coroutineContext

class RoundRobinTournamentMatchGenerator : MatchGenerator {
    override suspend fun generate(participants: ImmutableList<Participant>): ImmutableList<Round> {
        val circle = participants.map { Competitor.Fixed(it) }.toMutableList()
        val n = circle.size

        if (n < 2) {
            return persistentListOf()
        } else if (n == 2) {
            return persistentListOf(
                Round(
                    Ordinal.first,
                    persistentListOf(Match(Ordinal.first, circle.first(), circle.second()))
                )
            )
        }

        val needBye = n % 2 != 0
        val fixed = if (needBye) null else circle.removeFirst()

        val numberOfRounds = if (needBye) n else n - 1

        var roundRank = Ordinal.first
        var rounds = persistentListOf<Round>()

        for (round in 1..numberOfRounds) {
            coroutineContext.ensureActive()

            var matchRanks = Ordinal.sequence
            var matches = persistentListOf<Match>()

            if (fixed != null) {
                matches = matches.add(Match(matchRanks.first(), fixed, circle.last()))
                matchRanks = matchRanks.drop(1)
            }

            val splitPoint = circle.size / 2
            val aCompetitors = circle.subList(0, splitPoint)
            val bCompetitors = circle.subList(splitPoint, if (needBye) circle.size else circle.size - 1).asReversed()

            matches = matches.addAll(
                matchRanks
                    .zip(aCompetitors.asSequence())
                    .zip(bCompetitors.asSequence()) { (rank, a), b -> Match(rank, a, b) }
                    .toImmutableList()
            )

            rounds = rounds.add(Round(roundRank, matches))
            roundRank = roundRank.next()

            circle.rotate()
        }

        return rounds
    }
}

private fun <E> List<E>.second() = if (size >= 2) this[1] else throw NoSuchElementException()
private fun <E> MutableList<E>.rotate() = this.add(0, this.removeLast())
