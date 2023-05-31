import ch.tutteli.atrium.api.fluent.en_GB.feature
import ch.tutteli.atrium.api.fluent.en_GB.toBeEmpty
import ch.tutteli.atrium.api.fluent.en_GB.toContainExactly
import ch.tutteli.atrium.api.fluent.en_GB.toEqual
import ch.tutteli.atrium.api.verbs.expect
import info.marozzo.tournament.core.Competitor
import info.marozzo.tournament.core.Match
import info.marozzo.tournament.core.Participant
import info.marozzo.tournament.core.matchgenerators.MatchGenerator
import info.marozzo.tournament.core.matchgenerators.SingleEliminationTournamentMatchGenerator
import kotlin.random.Random
import kotlin.test.Test

class SingleEliminationTournamentMatchGeneratorTests {

    @Test
    fun noParticipantResultsInNoMatches() {
        // Arrange
        val random = Random(1234)
        val participants = listOf<Participant>()
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
        val participants = listOf(Participant("1"))
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
        val participants = listOf(p1, p2)
        val expected = Match(Competitor.Fixed(p1), Competitor.Fixed(p2))
        val sut: MatchGenerator = SingleEliminationTournamentMatchGenerator(random)

        // Act
        val matches = sut.generate(participants)

        // Assert
        expect(matches) { toContainExactly(expected) }
    }
}
