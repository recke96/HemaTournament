package info.marozzo.tournament.desktop

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.mvikotlin.extensions.coroutines.states
import info.marozzo.tournament.desktop.application.idgenerators.idGeneratorsModule
import info.marozzo.tournament.desktop.application.stores.tournament.TournamentStore
import info.marozzo.tournament.desktop.application.stores.storesModule
import info.marozzo.tournament.desktop.components.CloseConfirmationDialog
import info.marozzo.tournament.desktop.i18n.LocalStrings
import info.marozzo.tournament.desktop.i18n.ProvideStrings
import info.marozzo.tournament.desktop.i18n.rememberStrings
import info.marozzo.tournament.desktop.theme.AppTheme
import org.koin.core.context.startKoin

fun main() {
    val koinApp = startKoin {
        modules(idGeneratorsModule, storesModule)
    }
    val tournamentStore = koinApp.koin.get<TournamentStore>()
    application {
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
                    TournamentApp(state, tournamentStore::accept)
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
