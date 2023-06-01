package info.marozzo.tournament.core.matchgenerators

import info.marozzo.tournament.core.Competitor
import info.marozzo.tournament.core.Match
import info.marozzo.tournament.core.Participant
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

class RoundRobinTournamentMatchGenerator : MatchGenerator {
    override fun generate(participants: Iterable<Participant>): ImmutableList<Match> {
        val circle = participants.map { Competitor.Fixed(it) }.toMutableList()
        val n = circle.size

        if (n < 2) {
            return persistentListOf()
        } else if (n == 2) {
            return persistentListOf(Match(circle.first(), circle.second()))
        }

        val needBye = n % 2 != 0
        val fixed = if (needBye) null else circle.removeFirst()

        val rounds = if (needBye) n else n - 1

        val matches = persistentListOf<Match>().builder()

        for (round in 1..rounds) {

            if (fixed != null) {
                matches.add(Match(fixed, circle.last()))
            }

            val splitPoint = circle.size / 2
            val aCompetitors = circle.subList(0, splitPoint)
            val bCompetitors = circle.subList(splitPoint, circle.size).asReversed()

            matches.addAll(aCompetitors.zip(bCompetitors) { a, b -> Match(a, b) })

            circle.rotate()
        }

        return matches.build()
    }
}

private fun <E> List<E>.second() = if (size >= 2) this[1] else throw NoSuchElementException()
private fun <E> MutableList<E>.rotate() = this.add(0, this.removeLast())
