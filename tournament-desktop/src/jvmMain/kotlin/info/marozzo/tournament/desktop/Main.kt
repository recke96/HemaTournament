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
import com.arkivanov.mvikotlin.extensions.coroutines.states
import info.marozzo.tournament.desktop.application.db.withDb
import info.marozzo.tournament.desktop.application.dirs.withAppDirs
import info.marozzo.tournament.desktop.application.logging.withLogging
import info.marozzo.tournament.desktop.application.stores.withStores
import info.marozzo.tournament.desktop.application.withClock
import info.marozzo.tournament.desktop.components.CloseConfirmationDialog
import info.marozzo.tournament.desktop.i18n.LocalStrings
import info.marozzo.tournament.desktop.i18n.ProvideStrings
import info.marozzo.tournament.desktop.i18n.rememberStrings
import info.marozzo.tournament.desktop.theme.AppTheme
import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger {  }

fun main() = SuspendApp {
    resourceScope {
        withClock {
            withAppDirs {
                withLogging {
                    logger.info { "Starting application" }
                    withDb {
                        withStores {
                            application(exitProcessOnExit = false) {
                                val applicationState by application.states.collectAsState(application.state)
                                val tournamentState by tournament.states.collectAsState(tournament.state)
                                val windowState = rememberWindowState(
                                    placement = WindowPlacement.Maximized, size = DpSize.Unspecified
                                )
                                val (isCloseRequested, setIsCloseRequested) = remember { mutableStateOf(false) }
                                ProvideStrings(rememberStrings(currentLanguageTag = Locale.current.toLanguageTag())) {
                                    Window(
                                        title = LocalStrings.current.appTitle,
                                        state = windowState,
                                        onCloseRequest = { setIsCloseRequested(true) },
                                    ) {
                                        AppTheme {
                                            TournamentApp(
                                                applicationState,
                                                application::accept,
                                                tournamentState,
                                                tournament::accept
                                            )
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
                    logger.info { "Shutting down..." }
                }
            }
        }
    }
}

