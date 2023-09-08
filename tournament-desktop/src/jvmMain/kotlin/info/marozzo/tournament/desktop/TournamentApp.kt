package info.marozzo.tournament.desktop

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import info.marozzo.tournament.desktop.application.stores.tournament.NoEventState
import info.marozzo.tournament.desktop.application.stores.tournament.TournamentIntent
import info.marozzo.tournament.desktop.application.stores.tournament.TournamentState
import info.marozzo.tournament.desktop.screens.NoEventScreen

@Composable
internal fun TournamentApp(tournament: TournamentState, accept: (TournamentIntent) -> Unit) =
    Scaffold { padding ->
        Row {
            NavigationRail(
                header = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                },
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ) {
                NavigationRailItem(
                    selected = false,
                    icon = { Icon(Icons.Outlined.Home, contentDescription = "home") },
                    onClick = {},
                    enabled = false
                )
            }
            Box(
                modifier = Modifier.padding(padding)
            ) {
                Crossfade(targetState = tournament) { state ->
                    when (state) {
                        is NoEventState -> NoEventScreen(accept)
                    }
                }
            }
        }
    }
