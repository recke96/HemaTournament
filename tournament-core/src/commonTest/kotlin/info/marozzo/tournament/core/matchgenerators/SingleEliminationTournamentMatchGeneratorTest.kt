package info.marozzo.tournament.core.matchgenerators

import info.marozzo.tournament.core.Competitor
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.withClue
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.test.Enabled
import io.kotest.inspectors.shouldForAll
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.or
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.instanceOf
import io.kotest.property.Arb
import io.kotest.property.PropertyTesting
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.set
import io.kotest.property.checkAll
import kotlinx.collections.immutable.toImmutableList
import participant
import kotlin.math.ceil
import kotlin.math.log2
import kotlin.random.Random

class SingleEliminationTournamentMatchGeneratorTest : FunSpec({

    val random = PropertyTesting.defaultSeed?.run(::Random) ?: Random
    val sut = SingleEliminationTournamentMatchGenerator(random)

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

            val expectedNumberOfRounds = ceil(log2(n.toDouble())).toInt()
            val expectedMatchesPerRound = matchesPerRound(n, expectedNumberOfRounds).toImmutableList()

            val results = sut.generate(participants)
            assertSoftly(results) {
                withClue({ "Expect ceil(log_2(n)) rounds (n=$n)" }) {
                    size.shouldBe(expectedNumberOfRounds)
                }

                withClue({ "Expect rounds to have $expectedMatchesPerRound matches" }) {
                    map { it.matches.size }.shouldContainExactly(expectedMatchesPerRound)
                }

                withClue({ "Expect all participants to fight in the first two rounds" }) {
                    take(2)
                        .flatMap { it.matches }
                        .flatMap { sequenceOf(it.a, it.b) }
                        .filterIsInstance<Competitor.Fixed>()
                        .map { it.participant }
                        .shouldContainExactlyInAnyOrder(participants)
                }

                withClue({ "Expect each round to contain winners of previous round" }) {
                    windowed(2, 1) { (previous, round) ->

                        val competitors = round.matches
                            .flatMap { (_, a, b) -> sequenceOf(a, b) }

                        competitors.shouldForAll { competitor ->
                            competitor should (instanceOf<Competitor.Fixed>() or instanceOf<Competitor.WinnerOf>())
                        }

                        competitors
                            .filterIsInstance<Competitor.WinnerOf>()
                            .map { it.match }
                            .shouldContainExactlyInAnyOrder(previous.matches)
                    }
                }
            }
        }
    }

    test("Test the report failures") {
        true.shouldBeFalse()
    }

    xtest("Test the report xignore") {
        true.shouldBeFalse()
    }

    test("Test the report errors") {
        throw IllegalStateException("xxxx")
    }
})

private fun matchesPerRound(n: Int, rounds: Int): Sequence<Int> = sequence {
    val p = 1 shl rounds // smallest power of 2 where all competitors fit
    val firstCompetitors = 2 * n - p

    yield(firstCompetitors / 2)

    var nn = (firstCompetitors / 2) + (n - firstCompetitors)

    while (nn >= 2) {
        nn /= 2
        yield(nn)
    }
}
