package info.marozzo.tournament.desktop.components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import info.marozzo.tournament.desktop.components.util.Responsive

@Composable
fun AppBar(
    onShowNavigationChange: () -> Unit = {},
) {
    TopAppBar(
        title = { Text("Tournament Planner") },
        navigationIcon = {
            Responsive(
                compact = {
                    IconButton(
                        onClick = { onShowNavigationChange() },
                    ) {
                        Icon(
                            Icons.Default.Menu,
                            contentDescription = "Menu",
                        )
                    }
                },
                expanded = {}
            )
        },
    )
}
