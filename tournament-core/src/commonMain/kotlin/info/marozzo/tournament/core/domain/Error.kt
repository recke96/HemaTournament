package info.marozzo.tournament.core.domain

sealed class Error(val message: String) {

    class Validation(val propertyPath: String, message: String) : Error(message)
    class NotFound<TId>(id: TId): Error("No entity with it $id found")
}
