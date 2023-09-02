package info.marozzo.tournament.desktop.application.idgenerators

import arrow.core.Either

interface IdGenerator<TError, TId> {
    fun generate(): Either<TError, TId>
}

fun <TError, TId, TResultId> IdGenerator<TError, TId>.map(
    id: (TId) -> TResultId
) = object : IdGenerator<TError, TResultId> {
    override fun generate(): Either<TError, TResultId> = this@map
        .generate()
        .map(id)
}
