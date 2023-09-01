package info.marozzo.tournament.desktop

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import info.marozzo.tournament.desktop.components.TournamentTopBar

@Composable
internal fun TournamentApp(state: TournamentStore.State, accept: (TournamentStore.Intent) -> Unit) = Scaffold(
    topBar = { TournamentTopBar(state.event, accept) },
) {

}
