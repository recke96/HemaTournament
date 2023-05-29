package info.marozzo.tournament.core

/**
 * Represents the results of a [Pairing].
 */
interface PairingResult {
    /**
     * Score of the [Pairing.home] [Participant].
     */
    val homeScore: Number

    /**
     * Score of the [Pairing.away] [Participant].
     */
    val awayScore: Number
}
