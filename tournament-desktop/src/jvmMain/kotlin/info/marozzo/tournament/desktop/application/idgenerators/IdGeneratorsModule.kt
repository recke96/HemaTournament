package info.marozzo.tournament.desktop.application.idgenerators

import arrow.core.NonEmptyList
import info.marozzo.tournament.core.domain.EventId
import org.koin.dsl.module

val idGeneratorsModule = module {
    single<IdGenerator<NonEmptyList<NanoIdError>, NanoId>>() { NanoIdGenerator() }
    single<IdGenerator<NonEmptyList<NanoIdError>, EventId>>() {
        get<IdGenerator<NonEmptyList<NanoIdError>, NanoId>>().map {
            EventId(
                it.id
            )
        }
    }
}
