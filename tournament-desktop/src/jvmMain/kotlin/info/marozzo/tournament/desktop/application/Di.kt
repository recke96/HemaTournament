package info.marozzo.tournament.desktop.application

import info.marozzo.tournament.desktop.application.idgenerators.idGeneratorsModule
import info.marozzo.tournament.desktop.application.stores.storesModule
import org.koin.core.Koin
import org.koin.dsl.koinApplication

typealias DI = Koin

fun di(): DI = koinApplication {
    modules(idGeneratorsModule, storesModule)
}.koin
