package info.marozzo.tournament.desktop.components

import androidx.compose.animation.Crossfade
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import info.marozzo.tournament.desktop.components.util.LocalWidthClass

@Composable
fun TournamentPlannerAppBar() {
    val (isMenuOpen, setIsMenuOpen) = remember { mutableStateOf(false) }

    NavigationRail {  }

    TopAppBar(
        title = { Text("Tournament Planner ${LocalWidthClass.current}") },
        navigationIcon = {
            IconToggleButton(
                checked = isMenuOpen,
                onCheckedChange = { setIsMenuOpen(it) }) {
                Crossfade(targetState = isMenuOpen) {
                    Icon(
                        if (it) Icons.Default.Close else Icons.Default.Menu,
                        contentDescription = "Menu",
                    )
                }
            }
            DropdownMenu(
                expanded = isMenuOpen,
                onDismissRequest = { setIsMenuOpen(false) },
            ) {
                DropdownMenuItem(
                    onClick = { println("Test clicked") }
                ) {
                    Text("Test")
                }
            }
        },
    )


}
