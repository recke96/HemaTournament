package info.marozzo.tournament.core

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

/**
 * Used to generate [Pairing]s.
 */
interface PairingGenerator {

    /**
     * Indicates that all [Pairing]s have been generated.
     *
     * May be `true` after a single call to [generate] (e.g. Round-Robin)
     * or need multiple calls (e.g. Single-Elimination, Swiss-System).
     */
    val complete: Boolean

    /**
     *  Generate [Pairing]s.
     *  Should only return newly generated [Pairing]s.
     *
     *  @param results Takes results of previous rounds into account. Default is [emptyList], appropriate for the first call.
     */
    fun generate(results: Iterable<PairingResult> = emptyList()): ImmutableList<Pairing>
}

/**
 * Generates round-robin (all-go-away) [Pairing]s using the circle method.
 */
class RoundRobinPairingGenerator(private val tournament: Tournament) : PairingGenerator {

    private object Bye

    override var complete: Boolean = false
        private set

    override fun generate(results: Iterable<PairingResult>): ImmutableList<Pairing> {
        val n = tournament.participants.size
        val odd = n % 2 != 0
        val rounds = if (odd) n else n - 1

        if (complete || tournament.participants.size < 2) {
            return persistentListOf()
        }

        val pairings = persistentListOf<Pairing>().builder()

        val head = tournament.participants.head()

        // Cast to any, so we can add the `Bye` participant if necessary
        var tail = tournament.participants.tail().toPersistentList() as PersistentList<Any>

        // for odd number of participants we have to add the `Bye` participant
        if (odd) {
            tail = tail.add(Bye)
        }

        val tailSize = tail.size
        for (i in 0 until rounds) {

            // first pairing always involves the head
             val headPartner = tail.last()
            // only add if we don't have the `Bye`
            if (headPartner is Participant) {
                pairings.add(UndecidedPairing(head, headPartner))
            }

            val remainingGames = (tailSize - 1) / 2
            for (j in 0 until remainingGames) {
                val home = tail[j]
                val away = tail[tailSize - j - 2]

                // only add if we don't have the `Bye`
                if (home is Participant && away is Participant) {
                    pairings.add(UndecidedPairing(home, away))
                }
            }

            tail = tail.rotate()
        }


        complete = true
        return pairings.build()
    }
}
