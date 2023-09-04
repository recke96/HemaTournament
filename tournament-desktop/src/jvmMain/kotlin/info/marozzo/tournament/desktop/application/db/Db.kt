package info.marozzo.tournament.desktop.application.db

import app.cash.sqldelight.driver.jdbc.asJdbcDriver
import arrow.fx.coroutines.ResourceScope
import arrow.fx.coroutines.closeable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import info.marozzo.tournament.desktop.application.dirs.AppDirsContext
import info.marozzo.tournament.desktop.db.AppDb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal interface DbContext {
    val db: AppDb
}

context (ResourceScope, AppDirsContext)
internal suspend fun <T> withDb(block: suspend DbContext.() -> T): T {
    val dbFile = dataDir.resolve("appdata")
    val dataSource = closeable {
        HikariConfig().apply {
            dataSourceClassName = "org.h2.jdbcx.JdbcDataSource"
            username = "sa"
            password = ""
            addDataSourceProperty("URL", "jdbc:h2:$dbFile")
        }.let(::HikariDataSource)
    }
    val driver = closeable { dataSource.asJdbcDriver() }
    val context = object : DbContext {
        override val db = AppDb(driver)
    }

    val current = withContext(Dispatchers.IO) {
        runCatching {
            context.db.dbVersionQueries.selectLatest().executeAsOneOrNull()?.version
        }.getOrNull()
    }

    println("DB version:")
    println("Actual: $current | Schema: ${AppDb.Schema.version}")
    if (current == null || current != AppDb.Schema.version) {
        withContext(Dispatchers.IO) {
            AppDb.Schema.migrate(driver, current ?: 0L, AppDb.Schema.version)
        }
    }

    return context.block()
}
