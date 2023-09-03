package info.marozzo.tournament.desktop.application.stores.tournament

import org.koin.dsl.module

val tournamentModule = module {
    factory { TournamentExecutor() }
    single { TournamentReducer }
    single<TournamentStore> { TournamentStore.create(get(), { get() }, get()) }
}
