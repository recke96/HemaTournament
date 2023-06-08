package info.marozzo.tournament.desktop.components

import androidx.compose.animation.*
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
            compact = {
                val (showSettings, setShowSettings) = remember { mutableStateOf(true) }
                val (showMatches, setShowMatches) = remember { mutableStateOf(false) }

                Column(
                    modifier = Modifier.padding(
                        start = 5.dp,
                        top = padding.calculateTopPadding(),
                        end = 5.dp,
                        bottom = padding.calculateBottomPadding()
                    )
                ) {
                    ListItem(modifier = Modifier.clickable(onClickLabel = if (showSettings) "Close settings" else "Open settings") { setShowSettings(!showSettings) }) {
                        Text("Settings", style = MaterialTheme.typography.h4)
                        AnimatedVisibility(visible = showSettings, enter = slideInVertically(), exit = slideOutVertically()) {
                            Settings(
                                generator = generator,
                                onGeneratorChanged = { setGenerator(it) },
                                participants = participants,
                                onParticipantAdd = { setParticipants(participants.add(0, it)) },
                                onParticipantRemove = { setParticipants(participants.remove(it)) },
                            )
                        }
                    }
                    Divider()
                    ListItem(modifier = Modifier.clickable(onClickLabel = if (showMatches) "Close matches" else "Open matches") { setShowMatches(!showMatches) }) {
                        Text("Matches", style = MaterialTheme.typography.h4)
                        AnimatedVisibility(visible = showMatches) {
                            Matches(
                                generator = generator,
                                participants = participants,
                            )
                        }
                    }
                }
            },
            expanded = {
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
                                top = padding.calculateTopPadding(),
                                end = 10.dp,
                                bottom = padding.calculateBottomPadding()
                            )
                        )
                    ) {
                        Row {
                            Settings(
                                generator = generator,
                                onGeneratorChanged = { setGenerator(it) },
                                participants = participants,
                                onParticipantAdd = { setParticipants(participants.add(0, it)) },
                                onParticipantRemove = { setParticipants(participants.remove(it)) },
                                modifier = Modifier.fillMaxWidth(0.5f).padding(end = 5.dp),
                            )
                            Matches(
                                generator = generator,
                                participants = participants,
                                modifier = Modifier.fillMaxWidth().padding(start = 5.dp),
                            )
                        }
                    }
                }
            }
        )

    }
}
