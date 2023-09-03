package info.marozzo.tournament.desktop.application.stores

import info.marozzo.tournament.desktop.application.stores.tournament.TournamentIntent
import info.marozzo.tournament.desktop.application.stores.tournament.TournamentStore

internal interface Intent

internal class AcceptFunction(
    private val tournament: TournamentStore
) {
    operator fun invoke(intent: Intent) = when(intent) {
        is TournamentIntent -> tournament.accept(intent)
        else -> Unit
    }
}
