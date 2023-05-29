package info.marozzo.tournament.core


/**
 *  A [Pairing] is a match-up of to [Participant]s.
 *
 *  One is playing [home] and is considered to have an advantage,
 *  while the other plays [away] and is considered disadvantaged.
 */
interface Pairing {
    val home: Participant
    val away: Participant
}

/**
 * A [Pairing] that has not been played yet.
 */
data class UndecidedPairing(
    override val home: Participant,
    override val away: Participant,
) : Pairing

/**
 * A [Pairing] that has been scored by hits of both [Participant]s.
 *
 * The scores are calculated by dividing set hits through received hits.
 */
data class HitsScoredPairing(
    override val home: Participant,
    override val away: Participant,
    val hitsByHome: Int,
    val hitsByAway: Int,
) : Pairing, PairingResult {

    override val awayScore: Double = hitsByAway.toDouble() / hitsByHome.toDouble()
    override val homeScore: Double = hitsByHome.toDouble() / hitsByAway.toDouble()

}
