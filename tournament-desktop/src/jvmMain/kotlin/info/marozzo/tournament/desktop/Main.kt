package info.marozzo.tournament.desktop

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import info.marozzo.tournament.desktop.components.App


fun main() = application {
    Window(
        title = "Tournament Planner",
        onCloseRequest = ::exitApplication
    ) {
        App()
    }
}
