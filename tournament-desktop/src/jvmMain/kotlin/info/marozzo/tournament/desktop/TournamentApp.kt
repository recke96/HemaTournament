package info.marozzo.tournament.desktop

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import info.marozzo.tournament.desktop.application.stores.AcceptFunction
import info.marozzo.tournament.desktop.screens.NoEventScreen
import info.marozzo.tournament.desktop.application.stores.tournament.NoEventState
import info.marozzo.tournament.desktop.application.stores.tournament.TournamentState

@Composable
internal fun TournamentApp(state: TournamentState, accept: AcceptFunction) =
    Scaffold { padding ->
        Row {
            NavigationRail(
                header = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                },
                modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                NavigationRailItem(
                    selected = false,
                    icon = { Icon(Icons.Default.Home, contentDescription = "home") },
                    onClick = {},
                    enabled = false
                )
            }
            Divider(
                modifier = Modifier.fillMaxHeight().widthIn(1.dp, 3.dp)
            )
            Box(
                modifier = Modifier.padding(padding)
            ) {
                Crossfade(targetState = state) { state ->
                    when (state) {
                        is NoEventState -> NoEventScreen(accept)
                    }
                }
            }
        }
    }
