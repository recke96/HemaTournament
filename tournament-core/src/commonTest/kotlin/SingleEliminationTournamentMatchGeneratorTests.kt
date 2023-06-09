@file:Suppress("INLINE_FROM_HIGHER_PLATFORM")

import ch.tutteli.atrium.api.fluent.en_GB.*
import ch.tutteli.atrium.api.verbs.expect
import info.marozzo.tournament.core.*
import info.marozzo.tournament.core.matchgenerators.MatchGenerator
import info.marozzo.tournament.core.matchgenerators.SingleEliminationTournamentMatchGenerator
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.test.runTest
import kotlin.random.Random
import kotlin.test.Test

class SingleEliminationTournamentMatchGeneratorTests {

    @Test
    fun noParticipantResultsInNoMatches() = runTest {
        // Arrange
        val random = Random(1234)
        val participants = persistentListOf<Participant>()
        val sut: MatchGenerator = SingleEliminationTournamentMatchGenerator(random)

        // Act
        val matches = sut.generate(participants)

        // Assert
        expect(matches).toBeEmpty()
    }

    @Test
    fun oneParticipantResultsInNoMatches() = runTest {
        // Arrange
        val random = Random(1234)
        val participants = persistentListOf(Participant("1"))
        val sut: MatchGenerator = SingleEliminationTournamentMatchGenerator(random)

        // Act
        val matches = sut.generate(participants)

        // Assert
        expect(matches).toBeEmpty()
    }

    @Test
    fun twoParticipantResultsInOneMatches() = runTest {
        // Arrange
        val random = Random(1234)
        val p1 = Participant("1")
        val p2 = Participant("2")
        val participants = persistentListOf(p1, p2)
        val expected = Round(
            Ordinal.first,
            persistentListOf(Match(Ordinal.first, Competitor.Fixed(p2), Competitor.Fixed(p1)))
        )
        val sut: MatchGenerator = SingleEliminationTournamentMatchGenerator(random)

        // Act
        val matches = sut.generate(participants)

        // Assert
        expect(matches) { toContainExactly(expected) }
    }

    @Test
    fun threeParticipantResultsInTwoMatches() = runTest {
        // Arrange
        val random = Random(1234)
        val p1 = Participant("1")
        val p2 = Participant("2")
        val p3 = Participant("3")
        val participants = persistentListOf(p1, p2, p3)
        val m1 = Match(Ordinal.first, Competitor.Fixed(p1), Competitor.Fixed(p3))
        val m2 = Match(Ordinal.first, Competitor.Fixed(p2), Competitor.WinnerOf(m1))
        val expected = persistentListOf(
            Round(Ordinal.first, persistentListOf(m1)),
            Round(Ordinal.first.next(), persistentListOf(m2))
        )
        val sut: MatchGenerator = SingleEliminationTournamentMatchGenerator(random)

        // Act
        val matches = sut.generate(participants)

        // Assert
        expect(matches).toContain.inAnyOrder.only.elementsOf(expected)
    }

    @Test
    fun fourParticipantResultsInThreeMatches() = runTest {
        // Arrange
        val random = Random(1234)
        val p1 = Participant("1")
        val p2 = Participant("2")
        val p3 = Participant("3")
        val p4 = Participant("4")
        val participants = persistentListOf(p1, p2, p3, p4)
        val m1 = Match(Ordinal.first, Competitor.Fixed(p3), Competitor.Fixed(p2))
        val m2 = Match(Ordinal.first.next(), Competitor.Fixed(p4), Competitor.Fixed(p1))
        val r1 = Round(Ordinal.first, persistentListOf(m1, m2))
        val m3 = Match(Ordinal.first,  Competitor.WinnerOf(m1), Competitor.WinnerOf(m2))
        val r2 = Round(Ordinal.first.next(), persistentListOf(m3))
        val expected = persistentListOf(r1, r2)
        val sut: MatchGenerator = SingleEliminationTournamentMatchGenerator(random)

        // Act
        val matches = sut.generate(participants)

        // Assert
        expect(matches).toContain.inAnyOrder.only.elementsOf(expected)
    }
}
