package info.marozzo.tournament.core

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.mutate

fun <T> List<T>.head(): T = first()

fun <T> List<T>.tail(): List<T> {
    require(isNotEmpty())
    return subList(1, count())
}

fun <T> PersistentList<T>.rotate(): PersistentList<T> = mutate {
    val last = it.removeLast()
    it.add(0, last)
}
