package info.marozzo.tournament.desktop.components

import androidx.compose.foundation.background
import androidx.compose.material.Colors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import info.marozzo.tournament.core.domain.Event
import info.marozzo.tournament.desktop.TournamentStore
import info.marozzo.tournament.desktop.i18n.LocalStrings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TournamentTopBar(event: Event?, accept: (TournamentStore.Intent) -> Unit, modifier: Modifier = Modifier) {
    TopAppBar(
        title = { NameComponent(event?.name, default = LocalStrings.current.appTitle) },
        navigationIcon = {
            val (isNavigationOpen, setNavigationOpen) = remember { mutableStateOf(false) }
            IconButton(onClick = { setNavigationOpen(!isNavigationOpen) }) {
                Icon(Icons.Default.Menu, contentDescription = LocalStrings.current.openNavigation)
            }
            DropdownMenu(
                expanded = isNavigationOpen,
                onDismissRequest = { setNavigationOpen(false) }
            ) {
                DropdownMenuItem(text = { Text(text = "New Event") }, onClick = { })
            }
        },
        modifier = modifier.background(MaterialTheme.colorScheme.primary)
    )
}
