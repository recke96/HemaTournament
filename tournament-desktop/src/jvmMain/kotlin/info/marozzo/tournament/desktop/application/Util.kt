package info.marozzo.tournament.desktop.application

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.swing.Swing
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

suspend fun <T> onIo(block: suspend CoroutineScope.() -> T): T = withContext(Dispatchers.IO, block)
suspend fun <T> onMain(block: suspend CoroutineScope.() -> T): T =
    withContext(Dispatchers.Swing as CoroutineContext, block)