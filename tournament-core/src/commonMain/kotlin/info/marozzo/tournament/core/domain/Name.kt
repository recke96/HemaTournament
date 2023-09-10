package info.marozzo.tournament.core.domain

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure

@JvmInline
value class Name private constructor(val value: String) {
    companion object {
        operator fun invoke(name: String, properties: Iterable<String> = listOf("$")): Either<Error.Validation, Name> =
            either {
                ensure(name.isNotBlank()) {
                    Error.Validation(
                        properties.joinToString(separator = "."),
                        "A name can't be empty"
                    )
                }
                Name(name)
            }
    }
}
