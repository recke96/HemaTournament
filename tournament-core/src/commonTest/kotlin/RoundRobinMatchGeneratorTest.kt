import info.marozzo.tournament.core.Competitor
import info.marozzo.tournament.core.matchgenerators.RoundRobinTournamentMatchGenerator
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.collections.*
import io.kotest.matchers.shouldBe
import io.kotest.property.Arb
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.set
import io.kotest.property.checkAll
import kotlinx.collections.immutable.toImmutableList

class RoundRobinMatchGeneratorTest : FunSpec({

    val sut = RoundRobinTournamentMatchGenerator()

    test("No Matches tournaments") {
        checkAll(Arb.set(Arb.participant(), 0..1).map { it.toImmutableList() }) { participants ->
            collect("number_participants", participants.size)

            sut.generate(participants.toImmutableList()).shouldBeEmpty()
        }
    }

    test("Tournaments") {
        checkAll(Arb.set(Arb.participant(), 2..100).map { it.toImmutableList() }) { participants ->
            val n = participants.size
            collect("number_participants", n)

            val expectedNumberOfRounds = if (n % 2 == 0) n - 1 else n
            val expectedTotalNumberOfMatches = (n * (n - 1)) / 2
            val expectedNumberOfMatchesPerRound = n / 2
            val result = sut.generate(participants)

            assertSoftly(result) {
                withClue({ "Expect n - 1 rounds if n is even and n rounds if n is odd (n=$n)" }) {
                    size shouldBe expectedNumberOfRounds
                }
                withClue({ "Expect (n * (n - 1))/2 number of matches (n=$n)" }) {
                    sumOf { it.matches.size } shouldBe expectedTotalNumberOfMatches
                }
                forAll { round ->
                    assertSoftly(round.matches) {
                        withClue({ "Expect (n - 1)/2 matches if n is even and n/2 rounds if n is odd (n=$n)" }) {
                            size shouldBe expectedNumberOfMatchesPerRound
                        }
                        val roundParticipants = flatMap { sequenceOf(it.a, it.b) }
                            .map { it as Competitor.Fixed }
                            .map { it.participant }

                        withClue({ "Every participant, except maybe one, should have a match in round ${round.rank}" }) {
                            roundParticipants
                                .shouldHaveAtLeastSize(n - 1)
                                .shouldHaveAtMostSize(n)
                                .shouldBeUnique()
                                .forAll { it.shouldBeIn(participants) }
                        }
                    }
                }
            }
        }
    }
})
