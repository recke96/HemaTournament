package info.marozzo.tournament.desktop.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import info.marozzo.tournament.desktop.TournamentStore
import info.marozzo.tournament.desktop.components.util.LocalWidthClass
import info.marozzo.tournament.desktop.components.util.Responsive
import info.marozzo.tournament.desktop.components.util.WidthClass
import info.marozzo.tournament.desktop.components.util.matchRowSize

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Preview
internal fun App(state: TournamentStore.State, accept: (TournamentStore.Intent) -> Unit) {
    val scaffoldState = rememberScaffoldState()
    val (showNav, setShowNav) = remember { mutableStateOf(false) }

    LaunchedEffect(showNav) {
        if (showNav) {
            scaffoldState.drawerState.open()
        } else {
            scaffoldState.drawerState.close()
        }
    }

    Scaffold(scaffoldState = scaffoldState,
        topBar = { AppBar(onShowNavigationChange = { setShowNav(true) }) },
        drawerContent = if (LocalWidthClass.current < WidthClass.Expanded) {
            {
                ListItem(modifier = Modifier.clickable(
                    role = Role.Button, onClickLabel = "Navigate Home"
                ) { println("HOME") }, icon = { Icon(Icons.Default.Home, contentDescription = null) }) {
                    Text("Home")
                }
                Divider()
                ListItem(modifier = Modifier.clickable(
                    role = Role.Button, onClickLabel = "Close Navigation"
                ) { setShowNav(false) }, icon = { Icon(Icons.Default.ArrowBack, contentDescription = null) }) {
                    Text("Close")
                }
            }
        } else null) { padding ->
        Responsive(compact = { CompactAppContent(state, accept, padding) },
            expanded = { ExpandedAppContent(state, accept, padding) })
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun CompactAppContent(
    state: TournamentStore.State,
    accept: (TournamentStore.Intent) -> Unit,
    padding: PaddingValues,
) {
    Column(
        modifier = Modifier.padding(
            start = 0.dp, top = padding.calculateTopPadding(), end = 0.dp, bottom = padding.calculateBottomPadding()
        ).fillMaxHeight()
    ) {
        ListItem {
            Column {
                val (show, setShow) = remember { mutableStateOf(true) }
                val animatedRotation by animateFloatAsState(targetValue = if (show) 180f else 0f)
                TextButton(onClick = { setShow(!show) }, modifier = Modifier.fillMaxWidth()) {
                    Text("Settings", style = MaterialTheme.typography.h4)
                    Icon(
                        Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier.matchRowSize().rotate(animatedRotation)
                    )
                }
                AnimatedVisibility(
                    visible = show
                ) {
                    Surface(shape = MaterialTheme.shapes.large, elevation = 4.dp) {
                        Settings(state.matchGenerator, state.participants, accept)
                    }
                }
            }
        }
        Divider()
        ListItem {
            Column {
                val (show, setShow) = remember { mutableStateOf(true) }
                val animatedRotation by animateFloatAsState(targetValue = if (show) 180f else 0f)
                TextButton(onClick = { setShow(!show) }, modifier = Modifier.fillMaxWidth()) {
                    Text("Matches", style = MaterialTheme.typography.h4)
                    Icon(
                        Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier.matchRowSize().rotate(animatedRotation)
                    )
                }
                AnimatedVisibility(visible = show) {
                    Surface(shape = MaterialTheme.shapes.large, elevation = 4.dp) {
                        Matches(state.rounds, state.results, accept)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun ExpandedAppContent(
    state: TournamentStore.State,
    accept: (TournamentStore.Intent) -> Unit,
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
                        Settings(state.matchGenerator, state.participants, accept)
                    }
                }
                Spacer(modifier = Modifier.widthIn(10.dp, 25.dp))
                Surface(shape = MaterialTheme.shapes.large, elevation = 8.dp) {
                    Column(modifier = Modifier.padding(5.dp).fillMaxHeight()) {
                        Text("Matches", style = MaterialTheme.typography.h4)
                        Spacer(modifier = Modifier.heightIn(10.dp, 25.dp))
                        Matches(state.rounds, state.results, accept)
                    }
                }
            }
        }
    }
}
