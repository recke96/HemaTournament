import ch.tutteli.atrium.api.fluent.en_GB.*
import ch.tutteli.atrium.api.verbs.expect
import info.marozzo.tournament.core.Competitor
import info.marozzo.tournament.core.Match
import info.marozzo.tournament.core.Participant
import info.marozzo.tournament.core.matchgenerators.MatchGenerator
import info.marozzo.tournament.core.matchgenerators.RoundRobinTournamentMatchGenerator
import kotlinx.collections.immutable.persistentListOf
import kotlin.test.Test

class RoundRobinTournamentMatchGeneratorTests {

    @Test
    fun noParticipantResultsInNoMatches() {
        // Arrange
        val participants = persistentListOf<Participant>()
        val sut: MatchGenerator = RoundRobinTournamentMatchGenerator()

        // Act
        val matches = sut.generate(participants)

        // Assert
        expect(matches).toBeEmpty()
    }

    @Test
    fun oneParticipantResultsInNoMatches() {
        // Arrange
        val participants = persistentListOf(Participant("1"))
        val sut: MatchGenerator = RoundRobinTournamentMatchGenerator()

        // Act
        val matches = sut.generate(participants)

        // Assert
        expect(matches).toBeEmpty()
    }

    @Test
    fun twoParticipantResultsInOneMatches() {
        // Arrange
        val p1 = Participant("1")
        val p2 = Participant("2")
        val participants = persistentListOf(p1, p2)
        val expected = Match(Competitor.Fixed(p1), Competitor.Fixed(p2))
        val sut: MatchGenerator = RoundRobinTournamentMatchGenerator()

        // Act
        val matches = sut.generate(participants)

        // Assert
        expect(matches) { toContainExactly(expected) }
    }

    @Test
    fun threeParticipantResultsInThreeMatches() {
        // Arrange
        val p1 = Participant("1")
        val p2 = Participant("2")
        val p3 = Participant("3")
        val participants = persistentListOf(p1, p2, p3)
        val expected = persistentListOf(
            Match(Competitor.Fixed(p2), Competitor.Fixed(p3)),
            Match(Competitor.Fixed(p1), Competitor.Fixed(p2)),
            Match(Competitor.Fixed(p3), Competitor.Fixed(p1)),
        )
        val sut: MatchGenerator = RoundRobinTournamentMatchGenerator()

        // Act
        val matches = sut.generate(participants)

        // Assert
        expect(matches).toContain.inAnyOrder.only.elementsOf(expected)
    }

    @Test
    fun fourParticipantResultsInSixMatches() {
        // Arrange
        val p1 = Participant("1")
        val p2 = Participant("2")
        val p3 = Participant("3")
        val p4 = Participant("4")
        val participants = persistentListOf(p1, p2, p3, p4)
        val expected = persistentListOf(
            Match(Competitor.Fixed(p1), Competitor.Fixed(p2)),
            Match(Competitor.Fixed(p1), Competitor.Fixed(p3)),
            Match(Competitor.Fixed(p1), Competitor.Fixed(p4)),
            Match(Competitor.Fixed(p2), Competitor.Fixed(p3)),
            Match(Competitor.Fixed(p2), Competitor.Fixed(p4)),
            Match(Competitor.Fixed(p3), Competitor.Fixed(p4)),
        )
        val sut: MatchGenerator = RoundRobinTournamentMatchGenerator()

        // Act
        val matches = sut.generate(participants)

        // Assert
        expect(matches).toContain.inAnyOrder.only.elementsOf(expected)
    }
}
