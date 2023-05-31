package info.marozzo.tournament.core.matchgenerators

import info.marozzo.tournament.core.Match
import info.marozzo.tournament.core.Participant
import kotlinx.collections.immutable.ImmutableList

interface MatchGenerator {

    fun generate(participants: Iterable<Participant>): ImmutableList<Match>

}
