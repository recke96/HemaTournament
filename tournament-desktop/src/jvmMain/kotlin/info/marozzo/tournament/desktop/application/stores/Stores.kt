package info.marozzo.tournament.desktop.application.stores

import arrow.fx.coroutines.ResourceScope
import com.arkivanov.mvikotlin.core.utils.setMainThreadId
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import info.marozzo.tournament.desktop.application.ClockContext
import info.marozzo.tournament.desktop.application.db.DbContext
import info.marozzo.tournament.desktop.application.onMain
import info.marozzo.tournament.desktop.application.stores.application.ApplicationBootstrapper
import info.marozzo.tournament.desktop.application.stores.application.ApplicationExecutor
import info.marozzo.tournament.desktop.application.stores.application.ApplicationState
import info.marozzo.tournament.desktop.application.stores.application.ApplicationStore
import info.marozzo.tournament.desktop.application.stores.tournament.TournamentExecutor
import info.marozzo.tournament.desktop.application.stores.tournament.TournamentReducer
import info.marozzo.tournament.desktop.application.stores.tournament.TournamentStore


internal interface StoresContext {
    val tournament: TournamentStore
    val application: ApplicationStore
}

context (ResourceScope, DbContext, ClockContext)
internal suspend fun <T> withStores(block: suspend StoresContext.() -> T): T {
    onMain { setMainThreadId(Thread.currentThread().id) }

    val factory = DefaultStoreFactory()
    val tournament = TournamentStore.create(factory, ::TournamentExecutor, TournamentReducer)
    val application = ApplicationStore.create(
        factory,
        ApplicationBootstrapper(driver, db),
        { ApplicationExecutor(db, clock) },
        OptimisticallyConcurrentReducer()
    )

    val context = object : StoresContext {
        override val tournament = tournament
        override val application = application
    }

    return context.block()
}
