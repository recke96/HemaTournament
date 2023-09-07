package info.marozzo.tournament.desktop.application.stores.application

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.db.SqlDriver
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import info.marozzo.tournament.desktop.application.onIo
import info.marozzo.tournament.desktop.application.stores.TrySetStateMessage
import info.marozzo.tournament.desktop.db.AppDb
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

internal typealias TrySetApplicationStateMsg = TrySetStateMessage<ApplicationState>

internal sealed interface ApplicationAction

data object InitializeDbAction : ApplicationAction

internal class ApplicationExecutor(
    private val driver: SqlDriver, private val db: AppDb, private val clock: Clock
) : CoroutineExecutor<ApplicationIntent, ApplicationAction, ApplicationState, TrySetApplicationStateMsg, Nothing>() {

    override fun executeAction(action: ApplicationAction, getState: () -> ApplicationState) {
        when (action) {
            is InitializeDbAction -> scope.launch { initialize(getState) }
        }
    }

    override fun executeIntent(intent: ApplicationIntent, getState: () -> ApplicationState) {
        when (intent) {
            is SetTheme -> scope.launch { onIo {
            } }

            is SetLanguage -> TODO()
        }
    }

    private suspend fun initialize(getState: () -> ApplicationState) {
        onIo {
            val latest = AppDb.Schema.version
            val current = runCatching {
                db.dbVersionQueries.selectLatest().executeAsOne()
            }.getOrNull() ?: 0L

            if (current < AppDb.Schema.version) {
                AppDb.Schema.migrate(driver, current, latest)
            }
        }

        db.settingsQueries.select().asFlow().mapToOne(Dispatchers.IO).collectLatest {
            val current = getState()
            val updated = when (current) {
                is InitializingState -> SettingsState(
                    it.version, it.languageTag, it.theme, persistentSetOf(), clock.now()
                )

                is SettingsState -> current.copy(
                    settingsVersion = it.version,
                    language = it.languageTag,
                    theme = it.theme,
                    timestamp = clock.now()
                )
            }

            dispatch(TrySetApplicationStateMsg(updated, current.timestamp))
        }
    }
}
