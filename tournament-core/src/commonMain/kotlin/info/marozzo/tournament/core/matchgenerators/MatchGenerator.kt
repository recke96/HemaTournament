package info.marozzo.tournament.core.matchgenerators

import info.marozzo.tournament.core.Match
import info.marozzo.tournament.core.Participant
import kotlinx.collections.immutable.ImmutableList

/**
 * Generates [Matches][Match] to be played for given [Participants][Participant].
 *
 * Used to implement different tournament strategies such as [SingleEliminationTournamentMatchGenerator].
 */
interface MatchGenerator {

    /**
     * Generate the matches for the given [participants].
     */
    suspend fun generate(participants: ImmutableList<Participant>): ImmutableList<Match>

}
