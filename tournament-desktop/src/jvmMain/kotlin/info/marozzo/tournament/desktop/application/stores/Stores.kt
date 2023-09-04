package info.marozzo.tournament.desktop.application.stores

import arrow.fx.coroutines.ResourceScope
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory

context (ResourceScope)
internal suspend fun storeFactory(): StoreFactory = install(
    { DefaultStoreFactory() },
    { _, _ -> }
)
