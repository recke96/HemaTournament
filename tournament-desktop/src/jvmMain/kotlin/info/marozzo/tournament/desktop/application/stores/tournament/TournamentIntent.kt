package info.marozzo.tournament.desktop.application.stores.tournament

import info.marozzo.tournament.desktop.application.stores.Intent

internal sealed interface TournamentIntent: Intent

data class CreateNewEvent(val name: String): TournamentIntent
