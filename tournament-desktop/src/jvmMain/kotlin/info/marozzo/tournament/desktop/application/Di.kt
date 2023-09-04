package info.marozzo.tournament.desktop.application

import arrow.fx.coroutines.ResourceScope
import info.marozzo.tournament.desktop.application.idgenerators.idGeneratorsModule
import info.marozzo.tournament.desktop.application.stores.storesModule
import org.koin.core.scope.Scope
import org.koin.dsl.koinApplication

typealias DI = Scope

context (ResourceScope)
suspend fun di(): DI {
    val app = install(
        { koinApplication { modules(idGeneratorsModule, storesModule) } },
        { app, _ -> app.close() }
    )
    return install(
        { app.koin.createScope<ResourceScope>("app", source = this@ResourceScope) },
        { scope, _ -> scope.close() }
    )
}
