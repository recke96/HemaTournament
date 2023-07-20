import info.marozzo.tournament.core.matchgenerators.RoundRobinTournamentMatchGenerator
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.PropertyTesting
import io.kotest.property.arbitrary.list
import io.kotest.property.arbitrary.map
import io.kotest.property.checkAll
import kotlinx.collections.immutable.toImmutableList

class RoundRobinMatchGeneratorTest : FunSpec({

    PropertyTesting

    val sut = RoundRobinTournamentMatchGenerator()

    context("No Matches tournaments") {
        checkAll(Arb.list(Arb.participant(), 0..1).map { it.toImmutableList() }) { participants ->
            collect("number_participants", participants.size)

            sut.generate(participants.toImmutableList()).shouldBeEmpty()
        }
    }

    context("Tournaments") {
        checkAll(Arb.list(Arb.participant(), 2..1000).map{ it.toImmutableList() }) { participants ->
            collect("number_participants", participants.size)

            val expectedNumberOfRounds = if (participants.size % 2 == 0) participants.size - 1 else participants.size
            val expectedTotalNumberOfMatches = (participants.size * (participants.size - 1)) / 2
            val expectedNumberOfMatchesPerRound = participants.size / 2
            val result = sut.generate(participants)

            assertSoftly(result) {
                size shouldBe expectedNumberOfRounds
                forAll { it.matches.size shouldBe expectedNumberOfMatchesPerRound }
                sumOf { it.matches.size } shouldBe expectedTotalNumberOfMatches
            }
        }
    }
})
