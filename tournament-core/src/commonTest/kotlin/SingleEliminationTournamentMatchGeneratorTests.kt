import ch.tutteli.atrium.api.fluent.en_GB.*
import ch.tutteli.atrium.api.verbs.expect
import info.marozzo.tournament.core.Competitor
import info.marozzo.tournament.core.Match
import info.marozzo.tournament.core.Participant
import info.marozzo.tournament.core.matchgenerators.MatchGenerator
import info.marozzo.tournament.core.matchgenerators.SingleEliminationTournamentMatchGenerator
import kotlinx.collections.immutable.persistentListOf
import kotlin.random.Random
import kotlin.test.Test

class SingleEliminationTournamentMatchGeneratorTests {

    @Test
    fun noParticipantResultsInNoMatches() {
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
    fun oneParticipantResultsInNoMatches() {
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
    fun twoParticipantResultsInOneMatches() {
        // Arrange
        val random = Random(1234)
        val p1 = Participant("1")
        val p2 = Participant("2")
        val participants = persistentListOf(p1, p2)
        val expected = Match(Competitor.Fixed(p1), Competitor.Fixed(p2))
        val sut: MatchGenerator = SingleEliminationTournamentMatchGenerator(random)

        // Act
        val matches = sut.generate(participants)

        // Assert
        expect(matches) { toContainExactly(expected) }
    }

    @Test
    fun threeParticipantResultsInTwoMatches() {
        // Arrange
        val random = Random(1234)
        val p1 = Participant("1")
        val p2 = Participant("2")
        val p3 = Participant("3")
        val participants = persistentListOf(p1, p2, p3)
        val m1 = Match(Competitor.Fixed(p1), Competitor.Fixed(p3))
        val m2 = Match(Competitor.Fixed(p2), Competitor.WinnerOf(m1))
        val expected = persistentListOf(m1, m2)
        val sut: MatchGenerator = SingleEliminationTournamentMatchGenerator(random)

        // Act
        val matches = sut.generate(participants)

        // Assert
        expect(matches).toContainExactlyElementsOf(expected)
    }

    @Test
    fun fourParticipantResultsInThreeMatches() {
        // Arrange
        val random = Random(1234)
        val p1 = Participant("1")
        val p2 = Participant("2")
        val p3 = Participant("3")
        val p4 = Participant("4")
        val participants = persistentListOf(p1, p2, p3, p4)
        val m1 = Match(Competitor.Fixed(p2), Competitor.Fixed(p3))
        val m2 = Match(Competitor.Fixed(p1), Competitor.Fixed(p4))
        val m3 = Match(Competitor.WinnerOf(m1), Competitor.WinnerOf(m2))
        val expected = persistentListOf(m1, m2, m3)
        val sut: MatchGenerator = SingleEliminationTournamentMatchGenerator(random)

        // Act
        val matches = sut.generate(participants)

        // Assert
        expect(matches).toContainExactlyElementsOf(expected)
    }
}
