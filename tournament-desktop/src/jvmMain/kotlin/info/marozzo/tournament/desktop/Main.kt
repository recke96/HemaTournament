package info.marozzo.tournament.desktop

import androidx.compose.runtime.*
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import arrow.continuations.SuspendApp
import arrow.fx.coroutines.resourceScope
import com.arkivanov.mvikotlin.core.utils.setMainThreadId
import com.arkivanov.mvikotlin.extensions.coroutines.states
import info.marozzo.tournament.desktop.application.di
import info.marozzo.tournament.desktop.application.stores.AcceptFunction
import info.marozzo.tournament.desktop.application.stores.tournament.TournamentStore
import info.marozzo.tournament.desktop.components.CloseConfirmationDialog
import info.marozzo.tournament.desktop.i18n.LocalStrings
import info.marozzo.tournament.desktop.i18n.ProvideStrings
import info.marozzo.tournament.desktop.i18n.rememberStrings
import info.marozzo.tournament.desktop.theme.AppTheme
import kotlinx.coroutines.withContext
import org.jetbrains.skiko.MainUIDispatcher

fun main() = SuspendApp {
    resourceScope {
        withContext(MainUIDispatcher) { setMainThreadId(Thread.currentThread().id) }
        with(di()) {
            val tournamentStore = get<TournamentStore>()
            withContext(MainUIDispatcher) { tournamentStore.init() }

            val accept = get<AcceptFunction>()

            application(exitProcessOnExit = false) {
                val state by tournamentStore.states.collectAsState(tournamentStore.state)
                val windowState = rememberWindowState(
                    placement = WindowPlacement.Maximized, size = DpSize.Unspecified
                )
                val (isCloseRequested, setIsCloseRequested) = remember { mutableStateOf(false) }
                ProvideStrings(rememberStrings(languageTag = Locale.current.toLanguageTag())) {
                    Window(
                        title = LocalStrings.current.appTitle,
                        state = windowState,
                        onCloseRequest = { setIsCloseRequested(true) },
                    ) {
                        AppTheme {
                            TournamentApp(state, accept)
                            CloseConfirmationDialog(
                                isCloseRequested = isCloseRequested,
                                onClose = { exitApplication() },
                                onDismiss = { setIsCloseRequested(false) }
                            )
                        }
                    }
                }
            }
        }
    }
}
