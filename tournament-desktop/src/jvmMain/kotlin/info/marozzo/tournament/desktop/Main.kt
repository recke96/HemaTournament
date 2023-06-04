package info.marozzo.tournament.desktop

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import info.marozzo.tournament.core.Match
import info.marozzo.tournament.core.Participant
import info.marozzo.tournament.core.matchgenerators.MatchGenerator
import info.marozzo.tournament.core.matchgenerators.RoundRobinTournamentMatchGenerator
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.splitpane.ExperimentalSplitPaneApi
import org.jetbrains.compose.splitpane.HorizontalSplitPane
import org.jetbrains.compose.splitpane.rememberSplitPaneState

@OptIn(ExperimentalSplitPaneApi::class)
@Composable
@Preview
fun App() {
    val splitPaneState = rememberSplitPaneState(initialPositionPercentage = 0.4f)
    val (generator, setGenerator) = remember { mutableStateOf<MatchGenerator>(RoundRobinTournamentMatchGenerator()) }
    val (participants, setParticipants) = remember { mutableStateOf(persistentListOf<Participant>()) }

    MaterialTheme {
        Scaffold(
            topBar = { TournamentPlannerAppBar() }
        ) { padding ->
            HorizontalSplitPane(modifier = Modifier.padding(padding), splitPaneState = splitPaneState) {
                first(minSize = 300.dp) {
                    Settings(
                        generator = generator,
                        onGeneratorChanged = { setGenerator(it) },
                        participants = participants,
                        onParticipantAdd = { setParticipants(participants.add(0, it)) },
                        onParticipantRemove = { setParticipants(participants.remove(it)) },
                        modifier = Modifier.padding(horizontal = 5.dp),
                    )
                }
                splitter {
                    visiblePart {
                        Box(
                            modifier = Modifier.background(Color.Black).fillMaxHeight().width(2.dp)
                        )
                    }
                }
                second(minSize = 300.dp) {
                    Matches(generator, participants, modifier = Modifier.padding(horizontal = 5.dp))
                }
            }
        }
    }
}

fun main() = application {
    Window(
        title = "Tournament Planner",
        onCloseRequest = ::exitApplication
    ) {
        App()
    }
}
