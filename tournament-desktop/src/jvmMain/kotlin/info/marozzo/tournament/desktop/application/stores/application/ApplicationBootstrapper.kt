package info.marozzo.tournament.desktop.application.stores.application

import app.cash.sqldelight.db.SqlDriver
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import info.marozzo.tournament.desktop.application.onMain
import info.marozzo.tournament.desktop.db.AppDb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.logging.log4j.kotlin.logger

internal sealed interface ApplicationAction
data object LoadSettingsFromDb : ApplicationAction
data object LoadRecentFileHistory : ApplicationAction

internal class ApplicationBootstrapper(private val driver: SqlDriver, private val db: AppDb) :
    CoroutineBootstrapper<ApplicationAction>() {

        companion object {
            val logger = logger(ApplicationBootstrapper::class.java.name)
        }

    override fun invoke() {
        scope.launch(Dispatchers.IO) {
            val latest = AppDb.Schema.version
            val current = runCatching {
                db.dbVersionQueries.selectLatest().executeAsOne()
            }


            if (current.getOrDefault(0L) < AppDb.Schema.version) {
                logger.info(current.exceptionOrNull()) { "Migrating from ${current.getOrDefault(0L)} to ${AppDb.Schema.version}" }
                AppDb.Schema.migrate(driver, current.getOrDefault(0L), latest)
            }

            onMain {
                dispatch(LoadSettingsFromDb)
                dispatch(LoadRecentFileHistory)
            }
        }
    }
}
