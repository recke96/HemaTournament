package info.marozzo.tournament.desktop.application.stores.application

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import info.marozzo.tournament.desktop.application.onMain
import info.marozzo.tournament.desktop.db.AppDb
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal sealed interface ApplicationAction
data object LoadSettingsFromDb : ApplicationAction
data object LoadRecentFileHistory : ApplicationAction

internal class ApplicationBootstrapper(private val driver: SqlDriver, private val db: AppDb) :
    CoroutineBootstrapper<ApplicationAction>() {

    private val logger = KotlinLogging.logger { }

    override fun invoke() {
        scope.launch(Dispatchers.IO) {
            val latest = AppDb.Schema.version

            val exists = driver.executeQuery(
                null,
                "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES tbl WHERE tbl.TABLE_NAME = 'DBVERSION'",
                mapper = {
                    QueryResult.Value(it.next().value && it.getLong(0)!! > 0)
                },
                0
            ).await()

            val current = if (exists) {
                db.dbVersionQueries.selectLatest().executeAsOne()
            } else 0L

            if (current < latest) {
                logger.atInfo {
                    message = "Migrating DB from version $current to version $latest"
                    payload = buildMap(2) {
                        put("currentVersion", current)
                        put("latestVersion", latest)
                    }
                }
                AppDb.Schema.migrate(driver, current, latest)
            }

            onMain {
                dispatch(LoadSettingsFromDb)
                dispatch(LoadRecentFileHistory)
            }
        }
    }
}
