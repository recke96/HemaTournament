package info.marozzo.tournament.desktop.application.stores.tournament

internal sealed interface TournamentIntent

data class CreateNewEvent(val name: String): TournamentIntent
