package info.marozzo.tournament.desktop.application.stores

import arrow.fx.coroutines.ResourceScope
import com.arkivanov.mvikotlin.core.utils.setMainThreadId
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import info.marozzo.tournament.desktop.application.stores.tournament.TournamentExecutor
import info.marozzo.tournament.desktop.application.stores.tournament.TournamentReducer
import info.marozzo.tournament.desktop.application.stores.tournament.TournamentStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.swing.Swing
import kotlinx.coroutines.withContext


internal interface StoresContext {
    val tournament: TournamentStore
}

context (ResourceScope)
internal suspend fun <T> withStores(block: suspend StoresContext.() -> T): T {
    withContext(Dispatchers.Swing) {
        setMainThreadId(Thread.currentThread().id)
    }

    val factory = DefaultStoreFactory()
    val tournament = TournamentStore.create(factory, ::TournamentExecutor, TournamentReducer)

    val context = object : StoresContext {
        override val tournament = tournament
    }

    return context.block()
}
