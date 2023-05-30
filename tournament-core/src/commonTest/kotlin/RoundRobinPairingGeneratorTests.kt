import ch.tutteli.atrium.api.fluent.en_GB.toContainExactly
import ch.tutteli.atrium.api.verbs.expect
import info.marozzo.tournament.core.*
import kotlinx.collections.immutable.toPersistentList
import kotlin.test.Test

class RoundRobinPairingGeneratorTests {

    @Test
    fun testGeneratesExpectedPairingsEven() {
        // Arrange
        val tournament = Tournament(
            "Test", (1..4).map { Participant(it.toString()) }.toPersistentList()
        )
        val sut: PairingGenerator = RoundRobinPairingGenerator(tournament)

        // Act
        val pairings = sut.generate()

        // Assert
        expect(pairings)
            .toContainExactly(
                // First round
                expectedPairing("1", "4"),
                expectedPairing("2", "3"),

                // Second round
                expectedPairing("1", "3"),
                expectedPairing("4", "2"),

                // Third round
                expectedPairing("1", "2"),
                expectedPairing("3", "4"),
            )
    }

    @Test
    fun testGeneratesExpectedPairingsOdd() {
        // Arrange
        val tournament = Tournament(
            "Test", (1..3).map { Participant(it.toString()) }.toPersistentList()
        )
        val sut: PairingGenerator = RoundRobinPairingGenerator(tournament)

        // Act
        val pairings = sut.generate()

        // Assert
        expect(pairings)
            .toContainExactly(
                // First round
                expectedPairing("2", "3"),

                // Second round
                expectedPairing("1", "3"),

                // Third round
                expectedPairing("1", "2"),
            )
    }

    private fun expectedPairing(home: String, away: String) = UndecidedPairing(Participant(home), Participant(away))
}
