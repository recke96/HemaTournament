package info.marozzo.tournament.desktop.application.stores.tournament

import androidx.compose.runtime.Immutable

@Immutable
internal sealed class TournamentState

internal data object NoEventState : TournamentState();
