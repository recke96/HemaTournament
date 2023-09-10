package info.marozzo.tournament.desktop.application.stores.application

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import info.marozzo.tournament.desktop.application.onIo
import info.marozzo.tournament.desktop.application.stores.TrySetStateMessage
import info.marozzo.tournament.desktop.db.AppDb
import info.marozzo.tournament.desktop.i18n.LanguageTag
import info.marozzo.tournament.desktop.screens.Screen
import info.marozzo.tournament.desktop.theme.Theme
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

internal typealias TrySetApplicationStateMsg = TrySetStateMessage<ApplicationState>


internal class ApplicationExecutor(
    private val db: AppDb, private val clock: Clock
) : CoroutineExecutor<ApplicationIntent, ApplicationAction, ApplicationState, TrySetApplicationStateMsg, Nothing>() {

    override fun executeAction(action: ApplicationAction, getState: () -> ApplicationState) {
        when (action) {
            is LoadSettingsFromDb -> scope.launch { loadAppSettings(getState) }
            is LoadRecentFileHistory -> scope.launch { loadRecentFileHistory(getState) }
        }
    }

    override fun executeIntent(intent: ApplicationIntent, getState: () -> ApplicationState) {
        when (intent) {
            is SetTheme -> scope.launch { updateTheme(intent.theme, getState) }
            is SetLanguage -> scope.launch { updateLanguage(intent.languageTag, getState) }
            is ChangeScreen -> updateScreen(intent.screen, getState)
        }
    }

    private suspend fun loadAppSettings(getState: () -> ApplicationState) {
        val (_, languageTag, theme) = onIo { db.settingsQueries.select().executeAsOne() }
        val current = getState()
        val new = current.copy(language = languageTag, theme = theme, timestamp = clock.now())

        dispatch(TrySetApplicationStateMsg(new, current.timestamp))
    }

    private suspend fun loadRecentFileHistory(getState: () -> ApplicationState) {
        val files = onIo { db.fileHistoryQueries.selectRecent(5).executeAsList() }
        val current = getState()
        val new = current.copy(recent = current.recent.addAll(files), timestamp = clock.now())

        dispatch(TrySetApplicationStateMsg(new, current.timestamp))
    }

    private fun updateScreen(screen: Screen, getState: () -> ApplicationState) {
        val current = getState()
        val new = current.copy(screen = screen, timestamp = clock.now())

        dispatch(TrySetApplicationStateMsg(new, current.timestamp))
    }

    private suspend fun updateTheme(theme: Theme, getState: () -> ApplicationState) {
        val current = getState()
        val new = current.copy(theme = theme, timestamp = clock.now())

        dispatch(TrySetApplicationStateMsg(new, current.timestamp))

        onIo { db.settingsQueries.updateTheme(getState().theme) }
    }

    private suspend fun updateLanguage(language: LanguageTag, getState: () -> ApplicationState) {
        val current = getState()
        val new = current.copy(language = language, timestamp = clock.now())

        dispatch(TrySetApplicationStateMsg(new, current.timestamp))

        onIo { db.settingsQueries.updateLanguage(getState().language) }
    }
}
