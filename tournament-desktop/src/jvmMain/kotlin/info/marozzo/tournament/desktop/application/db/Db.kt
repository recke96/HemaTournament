package info.marozzo.tournament.desktop.application.db

import app.cash.sqldelight.EnumColumnAdapter
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.asJdbcDriver
import arrow.fx.coroutines.ResourceScope
import arrow.fx.coroutines.closeable
import info.marozzo.tournament.desktop.application.dirs.AppDirsContext
import info.marozzo.tournament.desktop.db.AppDb
import info.marozzo.tournament.desktop.db.migrations.Settings
import info.marozzo.tournament.desktop.i18n.LanguageTag
import info.marozzo.tournament.desktop.theme.Theme
import org.h2.jdbcx.JdbcConnectionPool

internal interface DbContext {
    val driver: SqlDriver
    val db: AppDb
}

context (ResourceScope, AppDirsContext)
internal suspend fun <T> withDb(block: suspend DbContext.() -> T): T {
    val dbFile = dataDir.resolve("appdata")

    val dataSource = install(
        { JdbcConnectionPool.create("jdbc:h2:$dbFile", "sa", "") },
        { pool, _ -> pool.dispose() }
    )
    val driver = closeable { dataSource.asJdbcDriver() }
    val context = object : DbContext {
        override val driver = driver
        override val db = AppDb(
            driver,
            Settings.Adapter(
                languageTagAdapter = EnumColumnAdapter<LanguageTag>(),
                themeAdapter = EnumColumnAdapter<Theme>()
            )
        )
    }

    return context.block()
}
