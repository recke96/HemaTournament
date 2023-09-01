package info.marozzo.tournament.desktop

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import info.marozzo.tournament.desktop.components.CloseConfirmationDialog
import info.marozzo.tournament.desktop.theme.AppTheme

fun main() {
    val tournamentStore = TournamentStoreFactory(DefaultStoreFactory()).create()
    application {
        val state by tournamentStore.states.collectAsState(tournamentStore.state)
        val windowState = rememberWindowState(
            placement = WindowPlacement.Maximized, size = DpSize.Unspecified
        )
        val (isCloseRequested, setIsCloseRequested) = remember { mutableStateOf(false) }
        Window(
            title = "Tournament Planner",
            state = windowState,
            onCloseRequest = { setIsCloseRequested(true) },
        ) {
            AppTheme {
                CloseConfirmationDialog(
                    isCloseRequested = isCloseRequested,
                    onClose = { exitApplication() },
                    onDismiss = { setIsCloseRequested(false) }
                )
            }
        }
    }
}
