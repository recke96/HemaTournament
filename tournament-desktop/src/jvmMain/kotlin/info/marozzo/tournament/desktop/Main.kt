package info.marozzo.tournament.desktop

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import info.marozzo.tournament.desktop.components.App
import info.marozzo.tournament.desktop.components.CloseConfirmationDialog
import info.marozzo.tournament.desktop.components.util.WithWidthClass


@OptIn(ExperimentalMaterialApi::class)
fun main() = application {
    val windowState = rememberWindowState(
        placement = WindowPlacement.Maximized, size = DpSize.Unspecified
    )
    val (isCloseRequested, setIsCloseRequested) = remember { mutableStateOf(false) }

    Window(
        title = "Tournament Planner",
        state = windowState,
        onCloseRequest = { setIsCloseRequested(true) },
    ) {
        WithWidthClass {

            App()

            CloseConfirmationDialog(
                isCloseRequested = isCloseRequested,
                onClose = { exitApplication() },
                onDismiss = { setIsCloseRequested(false) }
            )
        }
    }
}
