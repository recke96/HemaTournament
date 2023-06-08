@file:Suppress("INLINE_FROM_HIGHER_PLATFORM")

import ch.tutteli.atrium.api.fluent.en_GB.*
import ch.tutteli.atrium.api.verbs.expect
import info.marozzo.tournament.core.*
import info.marozzo.tournament.core.matchgenerators.MatchGenerator
import info.marozzo.tournament.core.matchgenerators.RoundRobinTournamentMatchGenerator
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class RoundRobinTournamentMatchGeneratorTests {

    @Test
    fun noParticipantResultsInNoMatches() = runTest {
        // Arrange
        val participants = persistentListOf<Participant>()
        val sut: MatchGenerator = RoundRobinTournamentMatchGenerator()

        // Act
        val matches = sut.generate(participants)

        // Assert
        expect(matches).toBeEmpty()
    }

    @Test
    fun oneParticipantResultsInNoMatches() = runTest {
        // Arrange
        val participants = persistentListOf(Participant("1"))
        val sut: MatchGenerator = RoundRobinTournamentMatchGenerator()

        // Act
        val matches = sut.generate(participants)

        // Assert
        expect(matches).toBeEmpty()
    }

    @Test
    fun twoParticipantResultsInOneMatches() = runTest {
        // Arrange
        val p1 = Participant("1")
        val p2 = Participant("2")
        val participants = persistentListOf(p1, p2)
        val expected = Round(
            Ordinal.first, persistentListOf(Match(Ordinal.first, Competitor.Fixed(p1), Competitor.Fixed(p2)))
        )
        val sut: MatchGenerator = RoundRobinTournamentMatchGenerator()

        // Act
        val matches = sut.generate(participants)

        // Assert
        expect(matches) { toContainExactly(expected) }
    }

    @Test
    fun threeParticipantResultsInThreeMatches() = runTest {
        // Arrange
        val p1 = Participant("1")
        val p2 = Participant("2")
        val p3 = Participant("3")
        val participants = persistentListOf(p1, p2, p3)
        val expected = persistentListOf(
            Round(
                Ordinal.first,
                persistentListOf(Match(Ordinal.first, Competitor.Fixed(p1), Competitor.Fixed(p3)))
            ),
            Round(
                Ordinal.first.next(),
                persistentListOf(Match(Ordinal.first, Competitor.Fixed(p3), Competitor.Fixed(p2)))
            ),
            Round(
                Ordinal.first.next().next(),
                persistentListOf(Match(Ordinal.first, Competitor.Fixed(p2), Competitor.Fixed(p1)))
            )
        )
        val sut: MatchGenerator = RoundRobinTournamentMatchGenerator()

        // Act
        val matches = sut.generate(participants)

        // Assert
        expect(matches).toContain.inAnyOrder.only.elementsOf(expected)
    }

    @Test
    fun fourParticipantResultsInSixMatches() = runTest {
        // Arrange
        val p1 = Participant("1")
        val p2 = Participant("2")
        val p3 = Participant("3")
        val p4 = Participant("4")
        val participants = persistentListOf(p1, p2, p3, p4)
        val expected = persistentListOf(
            Round(
                Ordinal.first,
                persistentListOf(
                    Match(Ordinal.first, Competitor.Fixed(p1), Competitor.Fixed(p4)),
                    Match(Ordinal.first.next(), Competitor.Fixed(p2), Competitor.Fixed(p3))
                )
            ),

            Round(
                Ordinal.first.next(),
                persistentListOf(
                    Match(Ordinal.first, Competitor.Fixed(p1), Competitor.Fixed(p3)),
                    Match(Ordinal.first.next(), Competitor.Fixed(p4), Competitor.Fixed(p2))
                )
            ),

            Round(
                Ordinal.first.next().next(),
                persistentListOf(
                    Match(Ordinal.first, Competitor.Fixed(p1), Competitor.Fixed(p2)),
                    Match(Ordinal.first.next(), Competitor.Fixed(p3), Competitor.Fixed(p4))
                )
            ),
        )
        val sut: MatchGenerator = RoundRobinTournamentMatchGenerator()

        // Act
        val matches = sut.generate(participants)

        // Assert
        expect(matches).toContain.inAnyOrder.only.elementsOf(expected)
    }
}
