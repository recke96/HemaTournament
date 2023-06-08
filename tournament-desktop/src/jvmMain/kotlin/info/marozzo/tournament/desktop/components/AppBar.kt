package info.marozzo.tournament.desktop.components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import info.marozzo.tournament.desktop.components.util.LocalWidthClass
import info.marozzo.tournament.desktop.components.util.WidthClass

@Composable
fun AppBar(
    onShowNavigationChange: () -> Unit = {},
) {
    TopAppBar(
        title = { Text("Tournament Planner") },
        navigationIcon = if (LocalWidthClass.current < WidthClass.Expanded) {
            {
                IconButton(
                    onClick = { onShowNavigationChange() },
                ) {
                    Icon(
                        Icons.Default.Menu,
                        contentDescription = "Menu",
                    )
                }
            }
        } else null,
    )
}
