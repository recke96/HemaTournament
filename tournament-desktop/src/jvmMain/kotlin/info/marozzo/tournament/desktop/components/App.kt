package info.marozzo.tournament.desktop.components

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import info.marozzo.tournament.core.Participant
import info.marozzo.tournament.core.matchgenerators.MatchGenerator
import info.marozzo.tournament.core.matchgenerators.RoundRobinTournamentMatchGenerator
import kotlinx.collections.immutable.persistentListOf

@Composable
@Preview
fun App() {
    val (generator, setGenerator) = remember { mutableStateOf<MatchGenerator>(RoundRobinTournamentMatchGenerator()) }
    val (participants, setParticipants) = remember { mutableStateOf(persistentListOf<Participant>()) }

    MaterialTheme {
        Scaffold(
            topBar = { TournamentPlannerAppBar() }
        ) { padding ->

        }
    }
}
