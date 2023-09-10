package info.marozzo.tournament.desktop.application.stores.application

import arrow.fx.coroutines.ResourceScope
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import info.marozzo.tournament.desktop.application.ClockContext
import info.marozzo.tournament.desktop.application.onMain
import info.marozzo.tournament.desktop.i18n.LanguageTag
import info.marozzo.tournament.desktop.screens.Screen
import info.marozzo.tournament.desktop.theme.Theme
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.datetime.Clock

internal interface ApplicationStore : Store<ApplicationIntent, ApplicationState, Nothing> {
    companion object {
        private fun createInternal(
            storeFactory: StoreFactory,
            executorFactory: () -> ApplicationExecutor,
            bootstrapper: ApplicationBootstrapper,
            reducer: Reducer<ApplicationState, TrySetApplicationStateMsg>,
            clock: Clock
        ): ApplicationStore =
            object : ApplicationStore, Store<ApplicationIntent, ApplicationState, Nothing> by storeFactory.create(
                name = "ApplicationStore",
                initialState = ApplicationState(Screen.HOME, LanguageTag.SYSTEM, Theme.SYSTEM, persistentSetOf(), clock.now()),
                bootstrapper = bootstrapper,
                executorFactory = executorFactory,
                reducer = reducer
            ) {}

        context (ResourceScope, ClockContext)
        suspend fun create(
            storeFactory: StoreFactory,
            bootstrapper: ApplicationBootstrapper,
            executorFactory: () -> ApplicationExecutor,
            reducer: Reducer<ApplicationState, TrySetApplicationStateMsg>,
        ): ApplicationStore = install(
            { onMain { createInternal(storeFactory, executorFactory, bootstrapper, reducer, clock) } },
            { t, _ -> onMain { t.dispose() } }
        )
    }
}
