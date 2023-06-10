package info.marozzo.tournament.desktop.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import info.marozzo.tournament.core.Participant
import info.marozzo.tournament.core.matchgenerators.MatchGenerator
import info.marozzo.tournament.core.matchgenerators.RoundRobinTournamentMatchGenerator
import info.marozzo.tournament.desktop.components.util.LocalWidthClass
import info.marozzo.tournament.desktop.components.util.Responsive
import info.marozzo.tournament.desktop.components.util.WidthClass
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Preview
fun App() {
    val scaffoldState = rememberScaffoldState()
    val (showNav, setShowNav) = remember { mutableStateOf(false) }

    val (generator, setGenerator) = remember { mutableStateOf<MatchGenerator>(RoundRobinTournamentMatchGenerator()) }
    val (participants, setParticipants) = remember { mutableStateOf(persistentListOf<Participant>()) }

    LaunchedEffect(showNav) {
        if (showNav) {
            scaffoldState.drawerState.open()
        } else {
            scaffoldState.drawerState.close()
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { AppBar(onShowNavigationChange = { setShowNav(true) }) },
        drawerContent = if (LocalWidthClass.current < WidthClass.Expanded) {
            {
                ListItem(
                    modifier = Modifier.clickable(
                        role = Role.Button,
                        onClickLabel = "Navigate Home"
                    ) { println("HOME") },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) }) {
                    Text("Home")
                }
                Divider()
                ListItem(
                    modifier = Modifier.clickable(
                        role = Role.Button,
                        onClickLabel = "Close Navigation"
                    ) { setShowNav(false) },
                    icon = { Icon(Icons.Default.ArrowBack, contentDescription = null) }) {
                    Text("Close")
                }
            }
        } else null
    ) { padding ->
        Responsive(
            compact = { CompactAppContent(generator, setGenerator, participants, setParticipants, padding) },
            expanded = { ExpandedAppContent(generator, setGenerator, participants, setParticipants, padding) }
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun CompactAppContent(
    generator: MatchGenerator,
    setGenerator: (MatchGenerator) -> Unit,
    participants: PersistentList<Participant>,
    setParticipants: (PersistentList<Participant>) -> Unit,
    padding: PaddingValues,
) {
    val (showSettings, setShowSettings) = remember { mutableStateOf(true) }
    val (showMatches, setShowMatches) = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(
            start = 5.dp,
            top = padding.calculateTopPadding(),
            end = 5.dp,
            bottom = padding.calculateBottomPadding()
        ).fillMaxHeight()
    ) {
        ListItem(modifier = Modifier.clickable(onClickLabel = if (showSettings) "Close settings" else "Open settings") {
            setShowSettings(
                !showSettings
            )
        }) {
            Column {
                Text("Settings", style = MaterialTheme.typography.h4)
                AnimatedVisibility(
                    visible = showSettings
                ) {
                    Settings(
                        generator = generator,
                        onGeneratorChanged = { setGenerator(it) },
                        participants = participants,
                        onParticipantAdd = { setParticipants(participants.add(0, it)) },
                        onParticipantRemove = { setParticipants(participants.remove(it)) },
                    )
                }
            }
        }
        Divider()
        ListItem(modifier = Modifier.clickable(onClickLabel = if (showMatches) "Close matches" else "Open matches") {
            setShowMatches(
                !showMatches
            )
        }) {
            Column {
                Text("Matches", style = MaterialTheme.typography.h4)
                AnimatedVisibility(visible = showMatches) {
                    Matches(
                        generator = generator,
                        participants = participants,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ExpandedAppContent(
    generator: MatchGenerator,
    setGenerator: (MatchGenerator) -> Unit,
    participants: PersistentList<Participant>,
    setParticipants: (PersistentList<Participant>) -> Unit,
    padding: PaddingValues,
) {
    Row {
        NavigationRail {
            NavigationRailItem(
                selected = true,
                onClick = { println("HOME") },
                icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            )
        }
        Box(
            modifier = Modifier.padding(
                PaddingValues(
                    start = 10.dp,
                    top = padding.calculateTopPadding() + 10.dp,
                    end = 10.dp,
                    bottom = padding.calculateBottomPadding() + 10.dp
                )
            )
        ) {
            Row {
                Surface(shape = MaterialTheme.shapes.large, elevation = 8.dp) {
                    Column(modifier = Modifier.padding(5.dp).fillMaxHeight().fillMaxWidth(0.5f)) {
                        Text("Settings", style = MaterialTheme.typography.h4)
                        Spacer(modifier = Modifier.heightIn(10.dp, 25.dp))
                        Settings(
                            generator = generator,
                            onGeneratorChanged = { setGenerator(it) },
                            participants = participants,
                            onParticipantAdd = { setParticipants(participants.add(0, it)) },
                            onParticipantRemove = { setParticipants(participants.remove(it)) }
                        )
                    }
                }
                Spacer(modifier = Modifier.widthIn(10.dp, 25.dp))
                Surface(shape = MaterialTheme.shapes.large, elevation = 8.dp) {
                    Column(modifier = Modifier.padding(5.dp).fillMaxHeight()) {
                        Text("Matches", style = MaterialTheme.typography.h4)
                        Spacer(modifier = Modifier.heightIn(10.dp, 25.dp))
                        Matches(
                            generator = generator,
                            participants = participants,
                        )
                    }
                }
            }
        }
    }
}
