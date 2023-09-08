package info.marozzo.tournament.desktop.application.stores.application

import app.cash.sqldelight.db.SqlDriver
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import info.marozzo.tournament.desktop.application.onMain
import info.marozzo.tournament.desktop.db.AppDb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal sealed interface ApplicationAction
data object LoadSettingsFromDb : ApplicationAction
data object LoadRecentFileHistory : ApplicationAction

internal class ApplicationBootstrapper(private val driver: SqlDriver, private val db: AppDb) :
    CoroutineBootstrapper<ApplicationAction>() {
    override fun invoke() {
        scope.launch(Dispatchers.IO) {
            val latest = AppDb.Schema.version
            val current = runCatching {
                db.dbVersionQueries.selectLatest().executeAsOne()
            }.getOrNull() ?: 0L

            if (current < AppDb.Schema.version) {
                AppDb.Schema.migrate(driver, current, latest)
            }

            onMain {
                dispatch(LoadSettingsFromDb)
                dispatch(LoadRecentFileHistory)
            }
        }
    }
}
