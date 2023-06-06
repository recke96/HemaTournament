package info.marozzo.tournament.desktop

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import info.marozzo.tournament.desktop.components.App


fun main() = application {
    val windowState = rememberWindowState(
        placement = WindowPlacement.Maximized, size = DpSize.Unspecified
    )

    Window(
        title = "Tournament Planner",
        state = windowState,
        onCloseRequest = ::exitApplication,
    ) {

        App()
    }
}
