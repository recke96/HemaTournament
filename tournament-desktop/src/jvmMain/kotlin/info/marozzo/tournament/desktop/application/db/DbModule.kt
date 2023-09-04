package info.marozzo.tournament.desktop.application.db

import arrow.fx.coroutines.ResourceScope
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.koin.dsl.module
import org.koin.dsl.scoped

val dbModule = module {
    scope<ResourceScope> {
        scoped {
            get<ResourceScope>().install({
                HikariConfig().apply {

                }.let(::HikariDataSource)
            }, { ds, _ -> ds.close() })
        }

    }
}
